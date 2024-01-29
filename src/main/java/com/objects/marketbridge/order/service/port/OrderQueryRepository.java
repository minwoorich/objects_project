package com.objects.marketbridge.order.service.port;


import com.objects.marketbridge.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {

    Optional<Order> findById(Long orderId);

    Order findByOrderNo(String orderNo);

    Order findWithOrderDetailsAndProduct(Long orderId);

    Order findByIdWithOrderDetail(Long orderId);

    Optional<Order> findOrderWithDetailsAndProduct(Long orderId);

    List<Order> findDistinctWithDetailsByMemberId(Long memberId);

    Order findByTid(String tid);

}
