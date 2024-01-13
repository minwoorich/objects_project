package com.objects.marketbridge.domain.payment.repository;

import com.objects.marketbridge.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Payment findByProdOrderId(Long prodOrderId);
    Payment findByOrderNo(String orderNo);
}
