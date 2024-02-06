package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ConfirmCancelReturnHttp {

    @Getter
    @NoArgsConstructor
    public static class Request {

        @NotNull
        private List<OrderDetailInfo> orderDetailInfos;
        @NotNull
        private String cancelReason;

        @Builder
        public Request(List<OrderDetailInfo> orderDetailInfos, String cancelReason) {
            this.orderDetailInfos = orderDetailInfos;
            this.cancelReason = cancelReason;
        }

        public ConfirmCancelReturnDto.Request toDto() {
            return ConfirmCancelReturnDto.Request.builder()
                    .orderDetailInfos(getDtoOrderDetailInfos())
                    .cancelReason(cancelReason)
                    .build();
        }

        private List<ConfirmCancelReturnDto.OrderDetailInfo> getDtoOrderDetailInfos() {
            return orderDetailInfos.stream()
                    .map(OrderDetailInfo::of)
                    .toList();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class OrderDetailInfo {
        private Long orderDetailId;
        private Long numberOfCancellation;

        @Builder
        public OrderDetailInfo(Long orderDetailId, Long numberOfCancellation) {
            this.orderDetailId = orderDetailId;
            this.numberOfCancellation = numberOfCancellation;
        }

        public static ConfirmCancelReturnDto.OrderDetailInfo of(OrderDetailInfo orderDetailInfo) {
            return ConfirmCancelReturnDto.OrderDetailInfo.builder()
                    .orderDetailId(orderDetailInfo.getOrderDetailId())
                    .numberOfCancellation(orderDetailInfo.getNumberOfCancellation())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {
        private Long orderId;
        private String orderNo;
        private Long totalPrice;
        private LocalDateTime cancellationDate; // 주문 취소 일자
        private List<ProductInfo> cancelledItems;
        private RefundInfo refundInfo;

        @Builder
        private Response(Long orderId, String orderNo, Long totalPrice, LocalDateTime cancellationDate, RefundInfo refundInfo, List<ProductInfo> cancelledItems) {
            this.orderId = orderId;
            this.orderNo = orderNo;
            this.totalPrice = totalPrice;
            this.cancellationDate = cancellationDate;
            this.refundInfo = refundInfo;
            this.cancelledItems = cancelledItems;
        }

        public static Response of(ConfirmCancelReturnDto.Response serviceDto) {
            return Response.builder()
                    .orderId(serviceDto.getOrderId())
                    .orderNo(serviceDto.getOrderNo())
                    .totalPrice(serviceDto.getTotalPrice())
                    .cancellationDate(serviceDto.getCancellationDate())
                    .refundInfo(RefundInfo.of(serviceDto.getRefundInfo()))
                    .cancelledItems(
                            serviceDto.getCancelledItems().stream()
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

        public static ProductInfo of(ConfirmCancelReturnDto.ProductInfo productInfo) {
            return ProductInfo.builder()
                    .productId(productInfo.getProductId())
                    .name(productInfo.getName())
                    .price(productInfo.getPrice())
                    .quantity(productInfo.getQuantity())
                    .productNo(productInfo.getProductNo())
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
        private RefundInfo(Long totalRefundAmount, String refundMethod, LocalDateTime refundProcessedAt) {
            this.totalRefundAmount = totalRefundAmount;
            this.refundMethod = refundMethod;
            this.refundProcessedAt = refundProcessedAt;
        }

        public static RefundInfo of(ConfirmCancelReturnDto.RefundInfo refundDto) {
            return RefundInfo.builder()
                    .totalRefundAmount(refundDto.getTotalRefundAmount())
                    .refundMethod(refundDto.getRefundMethod())
                    .refundProcessedAt(refundDto.getRefundProcessedAt())
                    .build();
        }
    }
}
