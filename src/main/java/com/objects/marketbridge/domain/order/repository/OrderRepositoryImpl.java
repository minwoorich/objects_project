package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;


    @Override
    public Optional<ProdOrder> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public ProdOrder findByOrderNo(String orderNo) {
        return orderJpaRepository.findByOrderNo(orderNo).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public ProdOrder save(ProdOrder order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<ProdOrder> findWithOrderDetailsAndProduct(Long orderId) {
        return orderJpaRepository.findWithOrderDetailsAndProduct(orderId);
    }

    @Override
    public void deleteAllInBatch() {
        orderJpaRepository.deleteAllInBatch();
    }

    @Override
    public Optional<ProdOrder> findByIdWithOrderDetail(Long orderId) {
        return null;
    }
}
