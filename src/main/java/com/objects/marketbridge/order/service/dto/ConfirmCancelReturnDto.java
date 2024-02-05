package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ConfirmCancelReturnDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private String orderNo;
        private String cancelReason;

        @Builder
        private Request(String orderNo, String cancelReason) {
            this.orderNo = orderNo;
            this.cancelReason = cancelReason;
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

        public static Response of(Order order, RefundDto refundDto, DateTimeHolder dateTimeHolder) {
            return Response.builder()
                    .orderId(order.getId())
                    .orderNo(order.getOrderNo())
                    .totalPrice(
                            order.getOrderDetails()
                                    .stream()
                                    .mapToLong(OrderDetail::totalAmount)
                                    .sum()
                    )
                    .cancellationDate(dateTimeHolder.getUpdateTime(order))
                    .refundInfo(RefundInfo.of(refundDto))
                    .cancelledItems(
                            order.getOrderDetails().stream()
                                    .map(Response::getProductInfo)
                                    .toList()
                    )
                    .build();
        }

        private static ProductInfo getProductInfo(OrderDetail orderDetail) {
            return ProductInfo.of(orderDetail.getProduct(), orderDetail.getQuantity());
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

        public static ProductInfo of(Product product, Long quantity) {
            return ProductInfo.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(quantity)
                    .productNo(product.getProductNo())
                    .build();
        }

//        public static ProductInfo of(OrderDetail orderDetail) {
//            return ProductInfo.builder()
//                    .productId(orderDetail.getProduct().getId())
//                    .name(orderDetail.getProduct().getName())
//                    .price(orderDetail.getProduct().getPrice())
//                    .quantity(orderDetail.getQuantity())
//                    .productNo(orderDetail.getProduct().getProductNo())
//                    .build();
//        }
//
//        public static ProductInfo of(GetCancelReturnDetailDto.ProductInfo productInfo) {
//            return ProductInfo.builder()
//                    .productId(productInfo.getProductId())
//                    .name(productInfo.getName())
//                    .price(productInfo.getPrice())
//                    .quantity(productInfo.getQuantity())
//                    .productNo(productInfo.getProductNo())
//                    .build();
//        }
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
