package com.objects.marketbridge.cart.controller.dto;

import com.objects.marketbridge.cart.service.dto.CreateCartDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request{

        private String productNo;
        private Long quantity;
        private Boolean isSubs;


        @Builder
        private Request(String productNo, Long quantity,  Boolean isSubs) {
            this.productNo = productNo;
            this.quantity = quantity;
            this.isSubs = isSubs;
        }

        public static Request create(String productNo, Long quantity, Boolean isSubs) {
            return Request.builder()
                    .productNo(productNo)
                    .quantity(quantity)
                    .isSubs(isSubs)
                    .build();
        }

        public CreateCartDto toDto(Long memberId) {
            return CreateCartDto.create(productNo, memberId, quantity, isSubs);
        }
    }
}
