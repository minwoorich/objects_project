package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.domain.OrderCancelReturn;

public interface OrderCancelReturnCommendRepository {
    OrderCancelReturn save(OrderCancelReturn orderCancelReturn);
}
