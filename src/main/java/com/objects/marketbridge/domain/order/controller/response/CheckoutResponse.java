package com.objects.marketbridge.domain.order.controller.response;

import com.objects.marketbridge.domain.model.AddressValue;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckoutResponse {
    // 이름, 배송지, 연락처
    AddressValue address;
    // 보유 포인트
    Long pointBalance;

    String successUrl;
    String failUrl;

    @Builder
    public CheckoutResponse(AddressValue address, Long pointBalance, String successUrl, String failUrl) {
        this.address = address;
        this.pointBalance = pointBalance;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }


    public static CheckoutResponse from(AddressValue address, Long pointBalance, String successUrl, String failUrl) {
        return CheckoutResponse.builder()
                .address(address)
                .pointBalance(pointBalance)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }

}
