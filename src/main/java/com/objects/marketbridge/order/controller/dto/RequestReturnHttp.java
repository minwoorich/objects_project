package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RequestReturnHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<RequestReturnHttp.ProductInfoResponse> productResponses;
        private RequestReturnHttp.ReturnRefundInfoResponse returnRefundInfoResponse;

        @Builder
        private Response(List<RequestReturnHttp.ProductInfoResponse> productResponses, RequestReturnHttp.ReturnRefundInfoResponse returnRefundInfoResponse) {
            this.productResponses = productResponses;
            this.returnRefundInfoResponse = returnRefundInfoResponse;
        }

        public static Response of(RequestReturnDto.Response serviceDto) {
            return Response.builder()
                    .productResponses(
                            serviceDto.getProductInfos()
                                    .stream()
                                    .map(RequestReturnHttp.ProductInfoResponse::of)
                                    .toList()
                    )
                    .returnRefundInfoResponse(
                            RequestReturnHttp.ReturnRefundInfoResponse.of(serviceDto.getReturnRefundInfo())
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfoResponse {
        private Long quantity;
        private String name;
        private Long price;
        private String image; // TODO 주문 취소 요청 이미지 반환

        @Builder
        private ProductInfoResponse(Long quantity, String name, Long price, String image) {
            this.quantity = quantity;
            this.name = name;
            this.price = price;
            this.image = image;
        }

        private static ProductInfoResponse of(RequestReturnDto.ProductInfo serviceDto) {
            return ProductInfoResponse.builder()
                    .quantity(serviceDto.getQuantity())
                    .name(serviceDto.getName())
                    .price(serviceDto.getPrice())
                    .image(serviceDto.getImage())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReturnRefundInfoResponse {

        private Long deliveryFee;
        private Long returnFee;
        private Long productPrice;

        @Builder
        private ReturnRefundInfoResponse(Long deliveryFee, Long returnFee, Long productPrice) {
            this.deliveryFee = deliveryFee;
            this.returnFee = returnFee;
            this.productPrice = productPrice;
        }

        public static ReturnRefundInfoResponse of(RequestReturnDto.ReturnRefundInfo serviceDto) {
            return ReturnRefundInfoResponse.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .returnFee(serviceDto.getReturnFee())
                    .productPrice(serviceDto.getProductTotalPrice())
                    .build();
        }
    }

}
