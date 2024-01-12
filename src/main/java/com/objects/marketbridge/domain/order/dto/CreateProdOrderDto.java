package com.objects.marketbridge.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateProdOrderDto {
    private Long memberId;
    private Long addressId;
    private Long totalOrderPrice;

    @Builder
    public CreateProdOrderDto(Long memberId, Long addressId, Long totalOrderPrice) {
        this.memberId = memberId;
        this.addressId = addressId;
        this.totalOrderPrice = totalOrderPrice;
    }
}
