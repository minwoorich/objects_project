package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.domain.order.domain.Order;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.domain.order.domain.QOrder.order;
import static com.objects.marketbridge.domain.order.domain.QOrderDetail.orderDetail;

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
    public Order findByTid(String tid) {
        return orderJpaRepository.findByTid(tid);
    }

    @Override
    public List<Order> findDistinctWithDetailsByMemberId(Long memberId) {
        return null;
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
