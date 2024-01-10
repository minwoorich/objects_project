package com.objects.marketbridge.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreate {
    private Long ProductId;


    @Builder
    public OrderCreate(Long productId) {
        ProductId = productId;
    }
}
