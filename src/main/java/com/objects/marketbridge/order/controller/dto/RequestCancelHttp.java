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
        private List<ProductInfo> productInfos;
        private CancelRefundInfo cancelRefundInfo;

        @Builder
        private Response(List<ProductInfo> productInfos, CancelRefundInfo cancelRefundInfo) {
            this.productInfos = productInfos;
            this.cancelRefundInfo = cancelRefundInfo;
        }

        public static Response of(RequestCancelDto.Response serviceDto) {
            return Response.builder()
                    .productInfos(
                            serviceDto.getProductInfos()
                                    .stream()
                                    .map(ProductInfo::of)
                                    .toList()
                    )
                    .cancelRefundInfo(CancelRefundInfo.of(serviceDto.getCancelRefundInfo()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {
        private Long quantity;
        private String name;
        private Long price;
        private String image; // TODO 주문 취소 요청 이미지 반환

        @Builder
        private ProductInfo(Long quantity, String name, Long price, String image) {
            this.quantity = quantity;
            this.name = name;
            this.price = price;
            this.image = image;
        }

        public static ProductInfo of(RequestCancelDto.ProductInfo serviceDto) {
            return ProductInfo.builder()
                    .quantity(serviceDto.getQuantity())
                    .name(serviceDto.getName())
                    .price(serviceDto.getPrice())
                    .image(serviceDto.getImage())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CancelRefundInfo {

        private Long deliveryFee;
        private Long refundFee;
        private Long discountPrice;
        private Long totalPrice;

        @Builder
        private CancelRefundInfo(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
            this.deliveryFee = deliveryFee;
            this.refundFee = refundFee;
            this.discountPrice = discountPrice;
            this.totalPrice = totalPrice;
        }

        public static CancelRefundInfo of(RequestCancelDto.CancelRefundInfo serviceDto) {
            return CancelRefundInfo.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .refundFee(serviceDto.getRefundFee())
                    .discountPrice(serviceDto.getDiscountPrice())
                    .totalPrice(serviceDto.getTotalPrice())
                    .build();
        }
    }


}
