package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.repository.OrderJpaRepository;

import java.util.Optional;

public interface OrderRepository {

    Optional<ProdOrder> findById(Long orderId);

    ProdOrder save(ProdOrder order);
}
