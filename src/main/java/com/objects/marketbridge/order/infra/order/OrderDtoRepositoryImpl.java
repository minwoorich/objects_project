package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_OrderDetailInfo;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_Response;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.domain.QAddress.address;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;
import static com.objects.marketbridge.product.domain.QProduct.product;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.*;
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
    @Transactional(readOnly = true)
    public Page<GetCancelReturnListDtio.Response> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<GetCancelReturnListDtio.Response> content = getOrderCancelReturnListResponses(memberId);
        Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> orderDetailResponseMap = getOrderDetailResponseMap(findOrderNos(content));
        orderDetailResponseSetting(content, orderDetailResponseMap);

        JPAQuery<GetCancelReturnListDtio.Response> countQuery = getCountQuery(memberId);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<GetCancelReturnListDtio.Response> getOrderCancelReturnListResponses(Long memberId) {
        List<GetCancelReturnListDtio.Response> content = queryFactory
                .select(
                        new QGetCancelReturnListDtio_Response (
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId))
                .fetch();
        return content;
    }

    private List<String> findOrderNos(List<GetCancelReturnListDtio.Response> content) {
        List<String> toOrderNos = content.stream()
                .map(GetCancelReturnListDtio.Response::getOrderNo)
                .toList();
        return toOrderNos;
    }

    private Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> getOrderDetailResponseMap(List<String> toOrderIds) {
        List<GetCancelReturnListDtio.OrderDetailInfo> detailResponseDtioList = queryFactory
                .select(
                        new QGetCancelReturnListDtio_OrderDetailInfo(
                                orderDetail.orderNo,
                                orderDetail.product.id,
                                orderDetail.product.productNo,
                                orderDetail.product.name,
                                orderDetail.product.price,
                                orderDetail.quantity,
                                orderDetail.statusCode
                        ))
                .from(orderDetail)
                .join(orderDetail.product, product)
                .where(
                        orderDetail.order.orderNo.in(toOrderIds),
                        orderDetail.statusCode.eq(ORDER_CANCEL.getCode())
                                .or(orderDetail.statusCode.eq(RETURN_COMPLETED.getCode()))
                ).fetch();


        return detailResponseDtioList.stream()
                .collect(Collectors.groupingBy(GetCancelReturnListDtio.OrderDetailInfo::getOrderNo));
    }

    private void orderDetailResponseSetting(List<GetCancelReturnListDtio.Response> content, Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> orderDetailResponseMap) {
        content.forEach(o -> o.changeOrderDetailInfos(orderDetailResponseMap.get(o.getOrderNo())));
    }

    private JPAQuery<GetCancelReturnListDtio.Response> getCountQuery(Long memberId) {
        JPAQuery<GetCancelReturnListDtio.Response> countQuery = queryFactory
                .select(new QGetCancelReturnListDtio_Response(
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId));
        return countQuery;
    }

    @Override
    public Page<OrderDtio> findByMemberIdWithMemberAddressNoFilter(Long memberId, Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .innerJoin(order.address, address)
                .innerJoin(order.member, member)
                .where(
                        eqMemberId(memberId)
                )
                .orderBy(
                        order.createdAt.desc()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 엔티티 -> dto 로 변환
        List<OrderDtio> orderDtios = orders.stream().map(OrderDtio::of).toList();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = createCountOrdersQueryNoFilter(memberId);

        return PageableExecutionUtils.getPage(orderDtios, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<OrderDtio> findByMemberIdWithMemberAddress(Condition condition, Pageable pageable) {
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
                .orderBy(
                        order.createdAt.desc()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // 엔티티 -> dto 로 변환
        List<OrderDtio> orderDtios = orders.stream().map(OrderDtio::of).toList();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = createCountOrdersQuery(condition);

        return PageableExecutionUtils.getPage(orderDtios, pageable, countQuery::fetchOne);
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

    private JPAQuery<Long> createCountOrdersQueryNoFilter(Long memberId) {
        return queryFactory
                .select(count(order))
                .from(order)
                .where(
                        eqMemberId(memberId)
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
}
