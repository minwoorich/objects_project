package com.objects.marketbridge.order.infra.ordercancelreturn;

import com.objects.marketbridge.order.domain.OrderCancelReturn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelReturnJpaRepository extends JpaRepository<OrderCancelReturn, Long> {
}
