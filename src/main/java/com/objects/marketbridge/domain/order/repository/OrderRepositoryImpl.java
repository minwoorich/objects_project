package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnListResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderDetailResponse;
import com.objects.marketbridge.domain.order.controller.response.QOrderCancelReturnListResponse;
import com.objects.marketbridge.domain.order.controller.response.QOrderDetailResponse;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderTemp;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.model.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.objects.marketbridge.domain.order.entity.QOrder.order;
import static com.objects.marketbridge.domain.order.entity.QOrderDetail.orderDetail;
import static com.objects.marketbridge.domain.order.entity.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.domain.order.entity.StatusCodeType.RETURN_COMPLETED;
import static com.objects.marketbridge.model.QMember.*;
import static com.objects.marketbridge.model.QProduct.product;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager em) {
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
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order findWithOrderDetailsAndProduct(Long orderId) {
        return orderJpaRepository.findWithOrderDetailsAndProduct(orderId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteAllInBatch() {
        orderJpaRepository.deleteAllInBatch();
    }

    @Override
    public Optional<Order> findOrderWithDetailsAndProduct(Long orderId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(order)
                        .join(order.orderDetails, orderDetail).fetchJoin()
                        .join(orderDetail.product, product).fetchJoin()
                        .where(order.id.eq(orderId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }

    @Override
    public Order findByIdWithOrderDetail(Long orderId) {
        return null;
    }


    @Override
    public void saveAll(List<Order> orders) {
        orderJpaRepository.saveAll(orders);
    }

    @Override
    public void deleteByOrderNo(String orderNo) {
        orderJpaRepository.deleteByOrderNo(orderNo);
    }



    //    @Override
//    public List<Order> findDistinctWithDetailsByMemberId(Long memberId) {
//
//        BooleanExpression statusCondition = orderDetail.statusCode.eq(ORDER_CANCEL.getCode());
//        BooleanExpression orCondition = statusCondition.or(orderDetail.statusCode.eq(RETURN_COMPLETED.getCode()));
//
//        return queryFactory
//                .selectDistinct(order)
//                .from(order)
//                .join(order.orderDetails, orderDetail).fetchJoin()
//                .where(
//                        order.member.id.eq(memberId),
//                        orCondition
//                )
//                .fetch();
//    }

}
