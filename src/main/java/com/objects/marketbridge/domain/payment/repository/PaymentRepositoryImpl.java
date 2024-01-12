package com.objects.marketbridge.domain.payment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl {
    private final PaymentJpaRepository paymentJpaRepository;
}
