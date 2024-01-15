package com.objects.marketbridge.domain.order.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TempOrderRequest {

    @NotNull
    private String orderId;

    @NotNull
    private Long amount;
}
