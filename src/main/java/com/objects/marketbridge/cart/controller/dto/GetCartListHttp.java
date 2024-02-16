package com.objects.marketbridge.cart.controller.dto;

import com.objects.marketbridge.cart.service.dto.GetCartDto;
import com.objects.marketbridge.common.interceptor.SliceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

public class GetCartListHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        SliceResponse<GetCartDto> cartItems;

        @Builder
        private Response(SliceResponse<GetCartDto> cartItems) {
            this.cartItems = cartItems;
        }

        public static Response of(Slice<GetCartDto> carts) {
            return Response.builder()
                    .cartItems(new SliceResponse<>(carts))
                    .build();
        }
    }
}
