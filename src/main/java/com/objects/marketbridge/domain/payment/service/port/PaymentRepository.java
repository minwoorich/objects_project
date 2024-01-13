package com.objects.marketbridge.domain.payment.service.port;


import com.objects.marketbridge.domain.payment.domain.Payment;

public interface PaymentRepository {
    void save(Payment payment);
    Payment findById(Long id);

    Payment findByOrderId(Long orderId);

    Payment findByOrderNo(String orderNo);

    void deleteAllInBatch();
}
