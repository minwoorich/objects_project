package com.objects.marketbridge.payment.infra;

import com.objects.marketbridge.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long orderId);
    Payment findByOrderNo(String orderNo);
}
