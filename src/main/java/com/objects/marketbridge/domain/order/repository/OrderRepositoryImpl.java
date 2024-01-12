package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
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
}
