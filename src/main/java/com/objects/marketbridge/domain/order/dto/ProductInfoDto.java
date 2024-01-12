package com.objects.marketbridge.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductInfoDto {
    @NotNull
    private Long productId;

    @Min(1)
    @NotNull
    private Long quantity;

    @NotNull
    private Long unitOrderPrice;

    private Long usedCouponId;

    @Builder
    public ProductInfoDto(Long productId, Long quantity, Long unitOrderPrice, Long usedCouponId) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitOrderPrice = unitOrderPrice;
        this.usedCouponId = usedCouponId;
    }
}
