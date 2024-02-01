package com.objects.marketbridge.mock;

import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;

import java.util.List;

public class FakeOrderDetailQueryRepository extends BaseFakeOrderDetailRepository implements OrderDetailQueryRepository {
    @Override
    public OrderDetail findById(Long id) {
        return null;
    }

    @Override
    public List<OrderDetail> findByProductId(Long id) {
        return null;
    }

    @Override
    public List<OrderDetail> findAll() {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrderNo(String orderNo) {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrder_IdAndProductIn(Long orderId, List<Product> products) {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrderNoAndProduct_IdIn(String orderNo, List<Long> productIds) {
        return getInstance().getData().stream()
                .filter(od -> od.getOrder().getOrderNo().equals(orderNo)
                        && productIds.contains(od.getProduct().getId()))
                .toList();
    }
}
