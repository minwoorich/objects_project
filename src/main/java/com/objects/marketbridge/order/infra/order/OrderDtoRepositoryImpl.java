package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDtio;
import com.objects.marketbridge.order.infra.dtio.DetailResponseDtio;
import com.objects.marketbridge.order.infra.dtio.QCancelReturnResponseDtio;
import com.objects.marketbridge.order.infra.dtio.QDetailResponseDtio;
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

import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;
import static com.objects.marketbridge.product.domain.QProduct.product;


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
    public Page<CancelReturnResponseDtio> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<CancelReturnResponseDtio> content = getOrderCancelReturnListResponses(memberId);
        Map<String, List<DetailResponseDtio>> orderDetailResponseMap = getOrderDetailResponseMap(findOrderNos(content));
        orderDetailResponseSetting(content, orderDetailResponseMap);

        JPAQuery<CancelReturnResponseDtio> countQuery = getCountQuery(memberId);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<CancelReturnResponseDtio> getOrderCancelReturnListResponses(Long memberId) {
        List<CancelReturnResponseDtio> content = queryFactory
                .select(
                        new QCancelReturnResponseDtio(
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId))
                .fetch();
        return content;
    }

    private List<String> findOrderNos(List<CancelReturnResponseDtio> content) {
        List<String> toOrderNos = content.stream()
                .map(CancelReturnResponseDtio::getOrderNo)
                .toList();
        return toOrderNos;
    }

    private Map<String, List<DetailResponseDtio>> getOrderDetailResponseMap(List<String> toOrderIds) {
        List<DetailResponseDtio> detailResponseDtioList = queryFactory
                .select(
                        new QDetailResponseDtio(
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
                .collect(Collectors.groupingBy(DetailResponseDtio::getOrderNo));
    }

    private void orderDetailResponseSetting(List<CancelReturnResponseDtio> content, Map<String, List<DetailResponseDtio>> orderDetailResponseMap) {
        content.forEach(o -> o.changeDetailResponsDaos(orderDetailResponseMap.get(o.getOrderNo())));
    }

    private JPAQuery<CancelReturnResponseDtio> getCountQuery(Long memberId) {
        JPAQuery<CancelReturnResponseDtio> countQuery = queryFactory
                .select(new QCancelReturnResponseDtio(
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId));
        return countQuery;
    }



}
