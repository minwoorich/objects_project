package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.order.domain.OrderTemp;
import com.objects.marketbridge.domain.order.domain.ProdOrder;

import java.util.Optional;

public interface OrderRepository {

    Optional<ProdOrder> findById(Long orderId);
    ProdOrder findByOrderNo(String orderNo);

    ProdOrder save(ProdOrder order);

    Optional<ProdOrder> findWithOrderDetailsAndProduct(Long orderId);

    void deleteAllInBatch();

    OrderTemp findOrderTempByOrderNo(String orderNo);

    OrderTemp save(OrderTemp orderTemp);
}
