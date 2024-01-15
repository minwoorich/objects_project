package com.objects.marketbridge.domain.payment.client;

import com.objects.marketbridge.domain.payment.dto.RefundInfoDto;
import com.objects.marketbridge.domain.payment.exception.exception.RefundRejectException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefundClient {
    public Optional<RefundInfoDto> refund(String 계좌번호, Float 환불금액) {
        try {
            // TODO 환불 API

        } catch (Exception e) {
            throw new RefundRejectException("환불거절 되었습니다. 다시 시도해주세요.", e);
        }

        return Optional.of(new RefundInfoDto());
    }
}
