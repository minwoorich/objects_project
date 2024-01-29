package com.objects.marketbridge.product.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductDeleteResponseDto {

    private Long productId;

    @Builder
    public ProductDeleteResponseDto(Long productId) {
        this.productId = productId;
    }
}
