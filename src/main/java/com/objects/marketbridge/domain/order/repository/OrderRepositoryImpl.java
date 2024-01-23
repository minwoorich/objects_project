package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.entity.OrderTemp;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.domain.order.entity.QOrder.order;
import static com.objects.marketbridge.domain.order.entity.QOrderDetail.orderDetail;
import static com.objects.marketbridge.model.QProduct.product;


@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderTempJpaRepository orderTempJpaRepository;

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderTempJpaRepository orderTempJpaRepository, EntityManager em) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderTempJpaRepository = orderTempJpaRepository;
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
    public Optional<Order> findWithOrderDetailsAndProduct(Long orderId) {
        return orderJpaRepository.findWithOrderDetailsAndProduct(orderId);
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
    public Optional<Order> findByIdWithOrderDetail(Long orderId) {
        return null;
    }

    @Override
    public OrderTemp findOrderTempByOrderNo(String orderNo) {
        return orderTempJpaRepository.findOrderTempByOrderNo(orderNo).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(OrderTemp orderTemp) {
        orderTempJpaRepository.save(orderTemp);
    }

    @Override
    public void saveAll(List<Order> orders) {
        orderJpaRepository.saveAll(orders);
    }

    @Override
    public void saveOrderTempAll(List<OrderTemp> orderTempList) {
        orderTempJpaRepository.saveAll(orderTempList);
    }
}
