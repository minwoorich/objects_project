package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import com.objects.marketbridge.order.service.dto.GetCancelReturnDetailDto;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ConfirmCancelReturnHttp {

    public static class Request {

        @NotNull
        private String orderNo;
        @NotNull
        private String cancelReason;

        @Builder
        public Request(String orderNo, String cancelReason) {
            this.orderNo = orderNo;
            this.cancelReason = cancelReason;
        }

        public ConfirmCancelReturnDto.Request toServiceRequest() {
            return ConfirmCancelReturnDto.Request.builder()
                    .orderNo(orderNo)
                    .cancelReason(cancelReason)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long orderId;
        private String orderNumber;
        private Long totalPrice;
        private LocalDateTime cancellationDate; // 주문 취소 일자
        private List<ConfirmCancelReturnDto.ProductInfo> cancelledItems;
        private ConfirmCancelReturnDto.RefundInfo refundInfo;

        @Builder
        private Response(Long orderId, String orderNumber, Long totalPrice, LocalDateTime cancellationDate, ConfirmCancelReturnDto.RefundInfo refundInfo, List<ConfirmCancelReturnDto.ProductInfo> cancelledItems) {
            this.orderId = orderId;
            this.orderNumber = orderNumber;
            this.totalPrice = totalPrice;
            this.cancellationDate = cancellationDate;
            this.refundInfo = refundInfo;
            this.cancelledItems = cancelledItems;
        }

        public static Response of(ConfirmCancelReturnDto.Response serviceDto) {
            return Response.builder()
                    .orderId(serviceDto.getOrderId())
                    .orderNumber(serviceDto.getOrderNo())
                    .totalPrice(serviceDto.getTotalPrice())
                    .cancellationDate(serviceDto.getCancellationDate())
                    .refundInfo(serviceDto.getRefundInfo())
                    .cancelledItems(serviceDto.getCancelledItems())
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
    public static class RefundInfo {

        private Long totalRefundAmount;
        private String refundMethod;
        private LocalDateTime refundProcessedAt; // 환불 일자

        @Builder
        public RefundInfo(Long totalRefundAmount, String refundMethod, LocalDateTime refundProcessedAt) {
            this.totalRefundAmount = totalRefundAmount;
            this.refundMethod = refundMethod;
            this.refundProcessedAt = refundProcessedAt;
        }

        public static RefundInfo of(RefundDto refundDto) {
            return RefundInfo.builder()
                    .totalRefundAmount(refundDto.getTotalRefundAmount())
                    .refundMethod(refundDto.getRefundMethod())
                    .refundProcessedAt(refundDto.getRefundProcessedAt())
                    .build();
        }
    }
}
