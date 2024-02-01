package com.objects.marketbridge.product.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductResponseDto {

    private Long productId;

    public CreateProductResponseDto(Long productId) {
        this.productId = productId;
    }

}
