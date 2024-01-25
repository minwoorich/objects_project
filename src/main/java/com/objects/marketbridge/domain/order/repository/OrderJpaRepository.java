package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderDetails.product")
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product WHERE o.id = :orderId")
    Optional<Order> findWithOrderDetailsAndProduct(@Param("orderId") Long orderId);

    Optional<Order> findByOrderNo(String orderNo);

    void deleteByOrderNo(String orderNo);



}
