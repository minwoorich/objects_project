package com.objects.marketbridge.order.service.port;


import com.objects.marketbridge.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderQueryRepository {

    Optional<Order> findById(Long orderId);

    Order findByOrderNo(String orderNo);

    Order findByOrderIdFetchJoin(Long orderId);

    Order findByOrderNoWithMember(String orderNo);

    Order findByOrderNoWithOrderDetailsAndProduct(String orderNo);
    Page<Order> findAllPaged(GetOrderHttp.Condition condition, Pageable pageable);

}
