package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
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
        private List<ProductResponse> productResponseList;
        private CancelRefundInfoResponse cancelRefundInfoResponse;


        @Builder
        private Response(LocalDateTime orderDate, String orderNo, List<ProductResponse> productResponseList, LocalDateTime cancelDate, String cancelReason, CancelRefundInfoResponse cancelRefundInfoResponse) {
            this.orderDate = orderDate;
            this.orderNo = orderNo;
            this.productResponseList = productResponseList;
            this.cancelDate = cancelDate;
            this.cancelReason = cancelReason;
            this.cancelRefundInfoResponse = cancelRefundInfoResponse;
        }

        public static Response of(GetCancelReturnDetailDto.Response serviceDto) {
            return Response.builder()
                    .orderDate(serviceDto.getOrderDate())
                    .cancelDate(serviceDto.getCancelDate())
                    .orderNo(serviceDto.getOrderNo())
                    .cancelReason(serviceDto.getCancelReason())
                    .cancelRefundInfoResponse(CancelRefundInfoResponse.of(serviceDto.getCancelRefundInfo()))
                    .productResponseList(
                            serviceDto.getProductInfos().stream()
                                    .map(ProductResponse::of)
                                    .toList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductResponse {

        private Long productId;
        private String productNo;
        private String name;
        private Long price;
        private Long quantity;

        @Builder
        private ProductResponse(Long productId, String productNo, String name, Long price,Long quantity) {
            this.productId = productId;
            this.productNo = productNo;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public static ProductResponse of(Product product, Long quantity) {
            return ProductResponse.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(quantity)
                    .productNo(product.getProductNo())
                    .build();
        }

        public static ProductResponse of(OrderDetail orderDetail) {
            return ProductResponse.builder()
                    .productId(orderDetail.getProduct().getId())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .quantity(orderDetail.getQuantity())
                    .productNo(orderDetail.getProduct().getProductNo())
                    .build();
        }

        public static ProductResponse of(GetCancelReturnDetailDto.ProductInfo productListResponseDto) {
            return ProductResponse.builder()
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

        public static CancelRefundInfoResponse of(GetCancelReturnDetailDto.CancelRefundInfo serviceDto) {
            return CancelRefundInfoResponse.builder()
                    .deliveryFee(serviceDto.getDeliveryFee())
                    .refundFee(serviceDto.getRefundFee())
                    .discountPrice(serviceDto.getDiscountPrice())
                    .totalPrice(serviceDto.getTotalPrice())
                    .build();
        }
    }
}
