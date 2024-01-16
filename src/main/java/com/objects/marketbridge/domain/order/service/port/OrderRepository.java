package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.order.domain.OrderTemp;
import com.objects.marketbridge.domain.order.domain.ProdOrder;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<ProdOrder> findById(Long orderId);
    ProdOrder findByOrderNo(String orderNo);

    ProdOrder save(ProdOrder order);

    void saveAll(List<ProdOrder> orders);
    Optional<ProdOrder> findWithOrderDetailsAndProduct(Long orderId);

    void deleteAllInBatch();

    OrderTemp findOrderTempByOrderNo(String orderNo);

    void save(OrderTemp orderTemp);

    void saveOrderTempAll(List<OrderTemp> orderTempList);

}
