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

    @Builder
    public CheckoutResponse(AddressValue address, Long pointBalance) {
        this.address = address;
        this.pointBalance = pointBalance;
    }

    public static CheckoutResponse from(AddressValue address, Long pointBalance) {
        return CheckoutResponse.builder()
                .address(address)
                .pointBalance(pointBalance).build();
    }

}
