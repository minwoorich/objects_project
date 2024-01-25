package com.objects.marketbridge.domain.order.service.port;


import com.objects.marketbridge.domain.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(Long orderId);
    Order findByOrderNo(String orderNo);

    Order save(Order order);

    void saveAll(List<Order> orders);
    Order findWithOrderDetailsAndProduct(Long orderId);

    void deleteAllInBatch();

    Order findByIdWithOrderDetail(Long orderId);

    Optional<Order> findOrderWithDetailsAndProduct(Long orderId);

    List<Order> findDistinctWithDetailsByMemberId(Long memberId);

    Order findByTid(String tid);

    void deleteByOrderNo(String orderNo);
    // TODO 영속성 문제? 있는 쿼리
//    List<Order> findDistinctWithDetailsByMemberId(Long memberId);
}
