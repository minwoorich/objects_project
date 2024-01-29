package com.objects.marketbridge.product.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateResponseDto {

    private Long productId;

    public ProductCreateResponseDto(Long productId) {
        this.productId = productId;
    }

}
