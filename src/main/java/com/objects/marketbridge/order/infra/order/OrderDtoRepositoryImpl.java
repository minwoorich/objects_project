package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.infra.dao.CancelReturnResponseDao;
import com.objects.marketbridge.order.infra.dao.DetailResponseDao;
import com.objects.marketbridge.order.infra.dao.QCancelReturnResponseDao;
import com.objects.marketbridge.order.infra.dao.QDetailResponseDao;
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
    public Page<CancelReturnResponseDao> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<CancelReturnResponseDao> content = getOrderCancelReturnListResponses(memberId);
        Map<String, List<DetailResponseDao>> orderDetailResponseMap = getOrderDetailResponseMap(findOrderIds(content));
        orderDetailResponseSetting(content, orderDetailResponseMap);

        JPAQuery<CancelReturnResponseDao> countQuery = getCountQuery(memberId);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<CancelReturnResponseDao> getOrderCancelReturnListResponses(Long memberId) {
        List<CancelReturnResponseDao> content = queryFactory
                .select(new QCancelReturnResponseDao(
                        order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId))
                .fetch();
        return content;
    }

    private Map<String, List<DetailResponseDao>> getOrderDetailResponseMap(List<String> toOrderIds) {
        List<DetailResponseDao> detailResponseDaoList = queryFactory
                .select(
                        new QDetailResponseDao(
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
                        orderDetail.statusCode.eq(StatusCodeType.ORDER_CANCEL.getCode())
                                .or(orderDetail.statusCode.eq(StatusCodeType.RETURN_COMPLETED.getCode()))
                ).fetch();


        return detailResponseDaoList.stream()
                .collect(Collectors.groupingBy(DetailResponseDao::getOrderNo));
    }

    private void orderDetailResponseSetting(List<CancelReturnResponseDao> content, Map<String, List<DetailResponseDao>> orderDetailResponseMap) {
        content.forEach(o-> o.changeDetailResponsDaos(orderDetailResponseMap.get(o.getOrderNo())));
    }

    private JPAQuery<CancelReturnResponseDao> getCountQuery(Long memberId) {
        JPAQuery<CancelReturnResponseDao> countQuery = queryFactory
                .select(new QCancelReturnResponseDao(
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId));
        return countQuery;
    }

    private List<String> findOrderIds(List<CancelReturnResponseDao> content) {
        List<String> toOrderIds = content.stream()
                .map(CancelReturnResponseDao::getOrderNo)
                .toList();
        return toOrderIds;
    }


}
