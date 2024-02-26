package com.objects.marketbridge.domains.cart.controller.dto;

import com.objects.marketbridge.domains.cart.service.dto.UpdateCartDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request{

        @Min(value = 1, message = "장바구니 수량은 1 이상 100 이하여야합니다")
        @Max(value = 100, message = "장바구니 수량은 1 이상 100 이하여야합니다")
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
