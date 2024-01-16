package com.objects.marketbridge.domain.order.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class TempOrderRequest {

    @NotNull
    private String orderId;

    @NotNull
    private Long amount;

    @NotNull
    private List<String> products;
}
