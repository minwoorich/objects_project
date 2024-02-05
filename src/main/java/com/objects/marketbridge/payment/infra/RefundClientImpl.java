package com.objects.marketbridge.payment.infra;

import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.payment.service.port.RefundClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundClientImpl implements RefundClient {

    private final KakaoPayService kakaoPayService;

    @Override
    public RefundDto refund(String tid, Integer cancelAmount) {
        return RefundDto.of(kakaoPayService.cancel(tid, cancelAmount));
    }
}
