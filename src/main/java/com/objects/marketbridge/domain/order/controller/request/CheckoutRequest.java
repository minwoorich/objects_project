package com.objects.marketbridge.domain.order.controller.request;

import com.objects.marketbridge.domain.order.entity.ProductValue;
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
    private ProductValue productValue;

    private String rewardType;

    @Builder

    public CheckoutRequest(String orderId, Long amount, Long addressId, String orderName, ProductValue productValue, String rewardType) {
        this.orderId = orderId;
        this.amount = amount;
        this.addressId = addressId;
        this.orderName = orderName;
        this.productValue = productValue;
        this.rewardType = rewardType;
    }
}
