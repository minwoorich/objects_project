package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.product.domain.QProduct.product;

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

    @Override
    public Order findByOrderNoWithOrderDetails(String orderNo) {
        return orderJpaRepository.findByOrderNoWithOrderDetails(orderNo).orElseThrow(EntityNotFoundException::new);
    }

    // orderId 로 가져오기
    @Override
    public Order findByIdWithOrderDetailsAndProduct(Long orderId) {
        return orderJpaRepository.findByIdWithOrderDetailsAndProduct(orderId).orElseThrow(EntityNotFoundException::new);
    }

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

    // orderNo 로 가져오기
    @Override
    public Order findByOrderNoWithOrderDetailsAndProduct(String orderNo) {
        return orderJpaRepository.findByOrderNoWithOrderDetailsAndProduct(orderNo).orElseThrow(EntityNotFoundException::new);
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
