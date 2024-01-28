package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.domain.order.domain.OrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.common.domain.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OrderDetailRepositoryImpl implements OrderDetailRepository {

    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderDetailRepositoryImpl(OrderDetailJpaRepository orderDetailJpaRepository, EntityManager em) {
        this.orderDetailJpaRepository = orderDetailJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public int changeAllType(Long orderId, String type) {
        return orderDetailJpaRepository.changeAllType(orderId, type);
    }

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetail) {
        return orderDetailJpaRepository.saveAll(orderDetail);
    }

    @Override
    public void addReason(Long orderId, String reason) {
        orderDetailJpaRepository.addReason(orderId, reason);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailJpaRepository.save(orderDetail);
    }

    @Override
    public void deleteAllInBatch() {
        orderDetailJpaRepository.deleteAllInBatch();
    }

    @Override
    public OrderDetail findById(Long id) {
        return orderDetailJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public List<OrderDetail> findByProductId(Long id) {
        return orderDetailJpaRepository.findByProductId(id);
    }

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailJpaRepository.findAll();
    }

    @Override
    public List<OrderDetail> findByOrderNo(String orderNo) {
        return orderDetailJpaRepository.findByOrderNo(orderNo);
    }

    @Override
    public List<OrderDetail> findByOrder_IdAndProductIn(Long orderId, List<Product> products) {
        return orderDetailJpaRepository.findByOrder_IdAndProductIn(orderId, products);
    }

    @Override
    public List<OrderDetail> findByOrderNoAndProduct_IdIn(String orderNo, List<Long> productIds) {
        return orderDetailJpaRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);
    }

}
