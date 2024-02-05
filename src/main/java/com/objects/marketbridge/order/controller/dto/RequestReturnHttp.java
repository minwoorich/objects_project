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
        private List<ProductInfo> productInfos;
        private ReturnRefundInfo returnRefundInfo;

        @Builder
        private Response(List<ProductInfo> productInfos, ReturnRefundInfo returnRefundInfo) {
            this.productInfos = productInfos;
            this.returnRefundInfo = returnRefundInfo;
        }

        public static Response of(RequestReturnDto.Response serviceDto) {
            return Response.builder()
                    .productInfos(
                            serviceDto.getProductInfos()
                                    .stream()
                                    .map(ProductInfo::of)
                                    .toList()
                    )
                    .returnRefundInfo(ReturnRefundInfo.of(serviceDto.getReturnRefundInfo()))
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

        public static ProductInfo of(RequestReturnDto.ProductInfo serviceDto) {
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
    public static class ReturnRefundInfo {

        private Long deliveryFee;
        private Long returnFee;
        private Long productTotalPrice;

        @Builder
        private ReturnRefundInfo(Long deliveryFee, Long returnFee, Long productTotalPrice) {
            this.deliveryFee = deliveryFee;
            this.returnFee = returnFee;
            this.productTotalPrice = productTotalPrice;
        }

        public static ReturnRefundInfo of(RequestReturnDto.ReturnRefundInfo serviceDto) {
            return ReturnRefundInfo.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .returnFee(serviceDto.getReturnFee())
                    .productTotalPrice(serviceDto.getProductTotalPrice())
                    .build();
        }
    }

}
