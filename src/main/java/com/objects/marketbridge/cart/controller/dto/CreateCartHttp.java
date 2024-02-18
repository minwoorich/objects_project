package com.objects.marketbridge.cart.controller.dto;

import com.objects.marketbridge.cart.service.dto.CreateCartDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request{

        private Long productId;
        private Long quantity;
        private Boolean isSubs;

        @Builder
        private Request(Long productId, Long quantity,  Boolean isSubs) {
            this.productId = productId;
            this.quantity = quantity;
            this.isSubs = isSubs;
        }

        public static Request create(Long productId, Long quantity, Boolean isSubs) {
            return Request.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .isSubs(isSubs)
                    .build();
        }

        public CreateCartDto toDto(Long memberId) {
            return CreateCartDto.create(productId, memberId, quantity, isSubs);
        }
    }
}
