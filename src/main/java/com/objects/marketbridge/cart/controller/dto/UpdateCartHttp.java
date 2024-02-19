package com.objects.marketbridge.cart.controller.dto;

import com.objects.marketbridge.cart.service.dto.UpdateCartDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request{

        @Min(value = 1, message = "장바구니 수량은 1 이상이여야합니다")
        @Max(value = 100, message = "장바구니 수량은 100 이하여야합니다")
        private Long quantity;

        @Builder
        private Request(Long quantity) {
            this.quantity = quantity;
        }

        public UpdateCartDto toDto(Long cartId) {
            return UpdateCartDto.builder()
                    .quantity(quantity)
                    .cartId(cartId)
                    .build();
        }
    }
}
