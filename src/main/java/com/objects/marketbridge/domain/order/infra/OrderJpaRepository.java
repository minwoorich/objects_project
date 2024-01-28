package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.domain.order.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderDetails.product")
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product WHERE o.id = :orderId")
    Optional<Order> findWithOrderDetailsAndProduct(@Param("orderId") Long orderId);

    Optional<Order> findByOrderNo(String orderNo);

    Order findByTid(String tid);

    void deleteByOrderNo(String orderNo);



}
