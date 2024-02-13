package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.domain.OrderCancelReturn;

public interface OrderCancelReturnQueryRepository {
    OrderCancelReturn findById(Long id);
}
