package com.objects.marketbridge.product.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteProductResponseDto {

    private Long productId;

    @Builder
    public DeleteProductResponseDto(Long productId) {
        this.productId = productId;
    }
}
