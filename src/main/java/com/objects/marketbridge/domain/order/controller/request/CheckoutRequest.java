package com.objects.marketbridge.domain.order.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckoutRequest {

    @NotNull
    private String orderId;

    @NotNull
    private Long amount;

    @NotNull
    private Long addressId;

    @NotNull
    private String orderName;

    @NotNull
    private String rewardType;

    // "[p1:4,p2:1]?[c1,c2]"
    // [상품아이디1:수량,상품아이디2:수량]?[사용된 쿠폰아이디1,사용된 쿠폰아이디1]
    // 띄어쓰기 x, 구분자는 ?
    @NotNull
    private String stringfyData;

    @Builder
    public CheckoutRequest(String orderId, Long amount, Long addressId, String orderName, String rewardType) {
        this.orderId = orderId;
        this.amount = amount;
        this.addressId = addressId;
        this.orderName = orderName;
        this.rewardType = rewardType;
    }
}
