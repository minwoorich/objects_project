package com.objects.marketbridge.product.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteProductRequestDto {

    @NotNull
    private Long productId;

    @Builder
    public DeleteProductRequestDto(Long productId) {
        this.productId = productId;
    }
}
