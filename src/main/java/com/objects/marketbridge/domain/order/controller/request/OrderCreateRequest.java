package com.objects.marketbridge.domain.order.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateRequest {

    @NotNull
    private final Long productId;

    @NotNull
    private final Long productOptionId;

    @Min(1)
    private final Integer amount;

    @Builder
    public OrderCreateRequest(Long productId, Long productOptionId, Integer amount) {
        this.productId = productId;
        this.productOptionId = productOptionId;
        this.amount = amount;
    }
}
