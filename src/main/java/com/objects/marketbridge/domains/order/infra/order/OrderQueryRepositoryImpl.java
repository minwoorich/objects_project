package com.objects.marketbridge.domains.order.infra.order;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.utils.MyQueryDslUtil;
import com.objects.marketbridge.domains.member.domain.QMember;
import com.objects.marketbridge.domains.order.domain.Order;
import com.objects.marketbridge.domains.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.domains.order.domain.QOrder;
import com.objects.marketbridge.domains.order.service.port.OrderQueryRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.RESOUCRE_NOT_FOUND;
import static com.objects.marketbridge.domains.member.domain.QAddress.address;
import static com.objects.marketbridge.domains.member.domain.QMember.*;
import static com.objects.marketbridge.domains.order.domain.QOrder.order;
import static com.objects.marketbridge.domains.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.domains.payment.domain.QPayment.payment;
import static com.objects.marketbridge.domains.product.domain.QProduct.product;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.selectOne;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderQueryRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager em) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderJpaRepository.findByOrderNo(orderNo).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public Order findByOrderNoWithMember(String orderNo) {
        return orderJpaRepository.findByOrderNoWithMember(orderNo).orElseThrow(EntityNotFoundException::new);
    }

    // orderNo 로 가져오기
    @Override
    public Order findByOrderNoWithOrderDetailsAndProduct(String orderNo) {
        return orderJpaRepository.findByOrderNoWithOrderDetailsAndProduct(orderNo).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Order> findAllPaged(GetOrderHttp.Condition condition, Pageable pageable) {
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

        // 카운트 쿼리
        JPAQuery<Long> countQuery = createCountOrdersQuery(condition);

        return PageableExecutionUtils.getPage(orders, pageable, countQuery::fetchOne);
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

    private JPAQuery<Long> createCountOrdersQuery(GetOrderHttp.Condition condition) {
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
    public Order findByOrderIdFetchJoin(Long orderId) {
        Order result = queryFactory
                .selectFrom(order)
                .innerJoin(order.address, address)
                .innerJoin(order.member, member)
                .innerJoin(order.payment, payment).fetchJoin()
                .innerJoin(order.orderDetails, orderDetail).fetchJoin()
                .innerJoin(orderDetail.product, product).fetchJoin()
                .where(
                        eqOrderId(orderId)
                )
                .fetchOne();
        if (result == null) {
            throw CustomLogicException.createBadRequestError(RESOUCRE_NOT_FOUND, "해당 주문은 존재하지 않습니다", LocalDateTime.now());
        }
        return result;
    }

    private BooleanExpression eqOrderId(Long orderId) {
        return order.id.eq(orderId);
    }
}
