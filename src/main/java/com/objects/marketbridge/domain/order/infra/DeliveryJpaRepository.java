package com.objects.marketbridge.domain.order.infra;

import com.objects.marketbridge.domain.order.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}
