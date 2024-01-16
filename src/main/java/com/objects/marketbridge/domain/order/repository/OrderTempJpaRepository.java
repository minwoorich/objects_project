package com.objects.marketbridge.domain.order.repository;

import com.objects.marketbridge.domain.order.domain.OrderTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTempJpaRepository extends JpaRepository<OrderTemp, Long> {
    OrderTemp findOrderTempByOrderNo(String orderNo);

}
