package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.model.ProdOrder;

import java.util.Optional;

public interface OrderRepository {

    Optional<ProdOrder> findById(Long orderId);

    ProdOrder save(ProdOrder order);
}
