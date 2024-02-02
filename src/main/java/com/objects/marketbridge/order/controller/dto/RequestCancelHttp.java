package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.RequestCancelDto;
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
                            serviceDto.getProductInfos()
                                    .stream()
                                    .map(ProductInfoResponse::of)
                                    .toList()
                    )
                    .cancelRefundInfoResponse(
                            CancelRefundInfoResponse.of(serviceDto.getCancelRefundInfo())
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

        private static ProductInfoResponse of(RequestCancelDto.ProductInfo serviceDto) {
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
    public static class CancelRefundInfoResponse {

        private Long deliveryFee;
        private Long refundFee;
        private Long discountPrice;
        private Long totalPrice;

        @Builder
        private CancelRefundInfoResponse(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
            this.deliveryFee = deliveryFee;
            this.refundFee = refundFee;
            this.discountPrice = discountPrice;
            this.totalPrice = totalPrice;
        }

        public static CancelRefundInfoResponse of(RequestCancelDto.CancelRefundInfo serviceDto) {
            return CancelRefundInfoResponse.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .refundFee(serviceDto.getRefundFee())
                    .discountPrice(serviceDto.getDiscountPrice())
                    .totalPrice(serviceDto.getTotalPrice())
                    .build();
        }
    }


}
