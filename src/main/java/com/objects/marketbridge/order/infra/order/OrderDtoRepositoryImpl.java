package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.infra.dtio.*;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.objects.marketbridge.common.domain.QProduct.product;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;


@Repository
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
                        new QGetCancelReturnListDtio_OrderDetailInfo (
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

}
