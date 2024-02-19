package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.domain.OrderDetail;

import java.util.List;

public interface OrderDetailCommendRepository {

    void save(OrderDetail orderDetail);

    OrderDetail saveAndReturnEntity(OrderDetail orderDetail);

    List<OrderDetail> saveAll(List<OrderDetail> orderDetail);

    int changeAllType(Long orderId, String type);

    void deleteAllInBatch();

}
