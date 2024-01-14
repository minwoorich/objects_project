package com.objects.marketbridge.domain.order.controller.response;

import com.objects.marketbridge.domain.model.AddressValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CheckoutResponse {
    // 이름, 배송지, 연락처
    List<AddressValue> addressList;
    // 보유 포인트
    Long pointBalance;

    @Builder
    public CheckoutResponse(List<AddressValue> addressList, Long pointBalance) {
        this.addressList = addressList;
        this.pointBalance = pointBalance;
    }
}
