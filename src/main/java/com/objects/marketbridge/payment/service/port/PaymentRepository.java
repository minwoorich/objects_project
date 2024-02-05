package com.objects.marketbridge.payment.service.port;


import com.objects.marketbridge.payment.domain.Payment;

public interface PaymentRepository {
    void save(Payment payment);
    Payment findById(Long id);

    Payment findByTid(String tid);

    Payment findByOrderId(Long orderId);

    Payment findByOrderNo(String orderNo);

    void deleteAllInBatch();
}
