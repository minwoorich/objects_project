package com.objects.marketbridge.order.infra;

import com.objects.marketbridge.order.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}
