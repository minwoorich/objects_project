package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.common.utils.MyQueryDslUtil;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_OrderDetailInfo;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_Response;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.payment.domain.QPayment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.objects.marketbridge.member.domain.QAddress.address;
import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.order.controller.dto.select.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;
import static com.objects.marketbridge.payment.domain.QPayment.payment;
import static com.objects.marketbridge.product.domain.QProduct.product;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.selectOne;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
@Repository
@Transactional(readOnly = true)
public class OrderDtoRepositoryImpl implements OrderDtoRepository {

    private final OrderJpaRepository orderJpaRepository;

    private final JPAQueryFactory queryFactory;

    public OrderDtoRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager em) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<OrderDtio> findAllPaged(Condition condition, Pageable pageable) {
        OrderSpecifier[] orderSpecifiers = createOrderSpecifierArray(pageable.getSort());
        List<Order> orders = queryFactory
                .selectFrom(order)
                .innerJoin(order.address, address)
                .innerJoin(order.member, member)
                .where(
                        selectOne()
                                .from(orderDetail)
                                .innerJoin(orderDetail.product, product)
                                .where(
                                        orderDetail.order.id.eq(order.id),
                                        containsKeyword(condition.getKeyword())
                                ).exists()
                        ,
                        eqMemberId(condition.getMemberId()),
                        eqYear(condition.getYear())
                )
                .orderBy(orderSpecifiers)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 엔티티 -> dto 로 변환
        List<OrderDtio> orderDtios = orders.stream().map(OrderDtio::of).toList();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = createCountOrdersQuery(condition);

        return PageableExecutionUtils.getPage(orderDtios, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier[] createOrderSpecifierArray(Sort sort) {
        ArrayList<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        sort.forEach(o -> {
            switch (o.getProperty()) {
                case "createdAt" :
                    orderSpecifiers.add(MyQueryDslUtil.getSortedColumn(o, order, "createdAt"));
                    break;

                default:
                    break;
            }
        });

        // 0 으로 하면 자동으로 배열의 크기를 지정해줌
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private JPAQuery<Long> createCountOrdersQuery(Condition condition) {
        return queryFactory
                .select(count(order))
                .from(order)
                .where(
                        selectOne()
                                .from(orderDetail)
                                .innerJoin(orderDetail.product, product)
                                .where(
                                        orderDetail.order.eq(order),
                                        containsKeyword(condition.getKeyword())
                                ).exists()
                        ,
                        eqMemberId(condition.getMemberId()),
                        eqYear(condition.getYear())
                );
    }

    private BooleanExpression eqYear(String year) {
        return hasText(year) ? order.createdAt.year().eq(Integer.parseInt(year)) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return hasText(keyword) ? orderDetail.product.name.contains(keyword) : null;
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return order.member.id.eq(memberId);
    }



    @Override
    public OrderDtio findByOrderNo(String orderNo) {
        Order orderEntity = queryFactory
                .selectFrom(order)
                .innerJoin(order.address, address)
                .innerJoin(order.member, member)
                .innerJoin(order.payment, payment).fetchJoin()
                .innerJoin(order.orderDetails, orderDetail).fetchJoin()
                .innerJoin(orderDetail.product, product).fetchJoin()
                .where(
                        eqOrderNo(orderNo)
                )
                .fetchOne();

        log.info("order는 null인가요? {}",orderEntity==null);

        // 엔티티 -> dto 로 변환
        assert orderEntity != null;

        return OrderDtio.of(orderEntity);
    }

    private BooleanExpression eqOrderNo(String orderNo) {
        return order.orderNo.eq(orderNo);
    }
}
