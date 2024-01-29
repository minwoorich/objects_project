package com.objects.marketbridge.order.service.port;


import com.objects.marketbridge.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderCommendRepository {

    Order save(Order order);

    void saveAll(List<Order> orders);

    void deleteAllInBatch();

    void deleteByOrderNo(String orderNo);
}
