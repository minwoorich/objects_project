package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.controller.response.CancelRefundInfoResponse;
import com.objects.marketbridge.order.controller.response.ProductInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RequestCancelHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<ProductInfoResponse> productResponses;
        private CancelRefundInfoResponse cancelRefundInfoResponse;

        @Builder
        private Response(List<ProductInfoResponse> productResponses, CancelRefundInfoResponse cancelRefundInfoResponse) {
            this.productResponses = productResponses;
            this.cancelRefundInfoResponse = cancelRefundInfoResponse;
        }

        public static Response of(RequestCancelDto.Response serviceDto) {
            return Response.builder()
                    .productResponses(
                            serviceDto.getProductInfoResponseDtos()
                                    .stream()
                                    .map(ProductInfoResponse::of)
                                    .toList()
                    )
                    .cancelRefundInfoResponse(
                            CancelRefundInfoResponse.of(serviceDto.getCancelRefundInfoResponseDto())
                    )
                    .build();
        }
    }
}
