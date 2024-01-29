package com.objects.marketbridge.order.service.port;


import com.objects.marketbridge.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {

    Optional<Order> findById(Long orderId);

    Order findByOrderNo(String orderNo);

    Order findWithOrderDetails(String orderNo);

    Order findWithOrderDetailsAndProduct(Long orderId);

//    Optional<Order> findWithOrderDetailsAndProduct(Long orderId);

    Order findWithOrderDetailsAndProduct(String orderNo);

    Order findByIdWithOrderDetail(Long orderId);

    List<Order> findDistinctWithDetailsByMemberId(Long memberId);

    Order findByTid(String tid);

}
