package com.objects.marketbridge.payment.infra;

import com.objects.marketbridge.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByOrderNo(String orderNo);
    Optional<Payment> findByTid(String tid);

}
