package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.GetCancelReturnDetailDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GetCancelReturnDetailHttp {

    @NoArgsConstructor
    @Getter
    public static class Response {
        private LocalDateTime orderDate;
        private LocalDateTime cancelDate;
        private String orderNo;
        private String cancelReason;
        private List<ProductInfo> productInfos;
        private CancelRefundInfo cancelRefundInfo;


        @Builder
        private Response(LocalDateTime orderDate, String orderNo, List<ProductInfo> productInfos, LocalDateTime cancelDate, String cancelReason, CancelRefundInfo cancelRefundInfo) {
            this.orderDate = orderDate;
            this.orderNo = orderNo;
            this.productInfos = productInfos;
            this.cancelDate = cancelDate;
            this.cancelReason = cancelReason;
            this.cancelRefundInfo = cancelRefundInfo;
        }

        public static Response of(GetCancelReturnDetailDto.Response serviceDto) {
            return Response.builder()
                    .orderDate(serviceDto.getOrderDate())
                    .cancelDate(serviceDto.getCancelDate())
                    .orderNo(serviceDto.getOrderNo())
                    .cancelReason(serviceDto.getCancelReason())
                    .cancelRefundInfo(CancelRefundInfo.of(serviceDto.getCancelRefundInfo()))
                    .productInfos(
                            serviceDto.getProductInfos().stream()
                                    .map(ProductInfo::of)
                                    .toList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {

        private Long productId;
        private String productNo;
        private String name;
        private Long price;
        private Long quantity;

        @Builder
        private ProductInfo(Long productId, String productNo, String name, Long price, Long quantity) {
            this.productId = productId;
            this.productNo = productNo;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public static ProductInfo of(GetCancelReturnDetailDto.ProductInfo productListResponseDto) {
            return ProductInfo.builder()
                    .productId(productListResponseDto.getProductId())
                    .name(productListResponseDto.getName())
                    .price(productListResponseDto.getPrice())
                    .quantity(productListResponseDto.getQuantity())
                    .productNo(productListResponseDto.getProductNo())
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

        public static CancelRefundInfo of(GetCancelReturnDetailDto.CancelRefundInfo serviceDto) {
            return CancelRefundInfo.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .refundFee(serviceDto.getRefundFee())
                    .discountPrice(serviceDto.getDiscountPrice())
                    .totalPrice(serviceDto.getTotalPrice())
                    .build();
        }
    }
}
