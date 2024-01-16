package com.objects.marketbridge.domain.payment.client;

import com.objects.marketbridge.domain.payment.dto.RefundInfoDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRefundClient implements RefundClient{
    @Override
    public Optional<RefundInfoDto> refund(String accountNo, Long refundPrice) {
        return Optional.empty();
    }
}
