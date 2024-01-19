package com.objects.marketbridge.domain.payment.client;

import com.objects.marketbridge.domain.payment.dto.RefundInfoDto;

import java.util.Optional;

public interface RefundClient {

    Optional<RefundInfoDto> refund(String accountNo, Long refundPrice);
}
