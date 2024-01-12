package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepository {

    private final OrderDetailJpaRepository orderDetailJpaRepository;

    @Override
    public int changeAllType(Long orderId, String type) {
        return orderDetailJpaRepository.changeAllType(orderId, type);
    }

    @Override
    public List<ProdOrderDetail> saveAll(List<ProdOrderDetail> orderDetail) {
        return orderDetailJpaRepository.saveAll(orderDetail);
    }

    @Override
    public void addReason(Long orderId, String reason) {
        orderDetailJpaRepository.addReason(orderId, reason);
    }

    @Override
    public void save(ProdOrderDetail prodOrderDetail) {
        orderDetailJpaRepository.save(prodOrderDetail);
    }

    @Override
    public void deleteAllInBatch() {
        orderDetailJpaRepository.deleteAllInBatch();
    }

    @Override
    public ProdOrderDetail findById(Long id) {
        return orderDetailJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public List<ProdOrderDetail> findByProductId(Long id) {
        return orderDetailJpaRepository.findByProductId(id);
    }

    @Override
    public List<ProdOrderDetail> findAll() {
        return orderDetailJpaRepository.findAll();
    }
}
