package com.objects.marketbridge.order.infra;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.common.domain.QProduct.product;

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
    public Order findWithOrderDetails(String orderNo) {
        return orderJpaRepository.findWithOrderDetailsByOrderNo(orderNo).orElseThrow(EntityNotFoundException::new);
    }

    // orderId 로 가져오기
    @Override
    public Order findWithOrderDetailsAndProduct(Long orderId) {
        return orderJpaRepository.findWithOrderDetailsAndProduct(orderId).orElseThrow(EntityNotFoundException::new);
    }

    // orderNo 로 가져오기
    @Override
    public Order findWithOrderDetailsAndProduct(String orderNo) {
        return orderJpaRepository.findWithOrderDetailsAndProduct(orderNo).orElseThrow(EntityNotFoundException::new);
    }

//    @Override
//    public Optional<Order> findOrderWithDetailsAndProduct(Long orderId) {
////        return Optional.ofNullable(
////                queryFactory
////                        .selectFrom(order)
////                        .join(order.orderDetails, orderDetail).fetchJoin()
////                        .join(orderDetail.product, product).fetchJoin()
////                        .where(order.id.eq(orderId))
////                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
////                        .fetchOne()
////        );
//        return null;
//    }

    @Override
    public Order findByTid(String tid) {
        return orderJpaRepository.findByTid(tid).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Order> findDistinctWithDetailsByMemberId(Long memberId) {
        return null;
    }

    @Override
    public Order findByIdWithOrderDetail(Long orderId) {
        return null;
    }

}
