package com.objects.marketbridge.mock;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;

import java.util.List;
import java.util.Optional;

public class FakeOrderQueryRepository extends BaseFakeOrderRepository implements OrderQueryRepository {

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.empty();
    }

    @Override
    public Order findByOrderNo(String orderNo) {
            return getInstance().data.stream()
                    .filter(order -> order.getOrderNo().equals(orderNo))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("orderNo와 일치하는 주문이 존재하지 않습니다."));
    }

    @Override
    public Order findByOrderNoWithOrderDetails(String orderNo) {
        return null;
    }

    @Override
    public Order findByIdWithOrderDetailsAndProduct(Long orderId) {
        return null;
    }

    @Override
    public Order findByOrderNoWithOrderDetailsAndProduct(String orderNo) {
        return null;
    }

    @Override
    public Order findByIdWithOrderDetail(Long orderId) {
        return null;
    }

    @Override
    public List<Order> findDistinctWithDetailsByMemberId(Long memberId) {
        return null;
    }

    @Override
    public Order findByTid(String tid) {
        return null;
    }
}
