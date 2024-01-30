package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final KakaoPayService kakaoPayService;

    @Override
    public RefundDto refund(String tid, Integer cancelAmount) {
        return RefundDto.of(kakaoPayService.cancel(tid, cancelAmount));
    }
}
