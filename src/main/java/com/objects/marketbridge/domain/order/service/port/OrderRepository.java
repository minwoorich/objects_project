package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.order.entity.OrderTemp;
import com.objects.marketbridge.domain.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(Long orderId);
    Order findByOrderNo(String orderNo);

    Order save(Order order);

    void saveAll(List<Order> orders);
    Optional<Order> findWithOrderDetailsAndProduct(Long orderId);

    void deleteAllInBatch();

    Optional<Order> findByIdWithOrderDetail(Long orderId);

    OrderTemp findOrderTempByOrderNo(String orderNo);

    void save(OrderTemp orderTemp);

    void saveOrderTempAll(List<OrderTemp> orderTempList);

    Optional<Order> findOrderWithDetailsAndProduct(Long orderId);
}
