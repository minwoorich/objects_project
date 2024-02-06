package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ConfirmCancelReturnDto {

    @Getter
    @NoArgsConstructor
    public static class Request {

        private List<OrderDetailInfo> orderDetailInfos;
        private String cancelReason;

        @Builder
        public Request(List<OrderDetailInfo> orderDetailInfos, String cancelReason) {
            this.orderDetailInfos = orderDetailInfos;
            this.cancelReason = cancelReason;
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

        public static Response of(ServiceDto serviceDto, DateTimeHolder dateTimeHolder) {
            List<OrderDetail> orderDetails = serviceDto.getOrderDetails();
            Map<Long, OrderDetailInfo> orderDetailInfoMap = serviceDto.getOrderDetailInfoMap();

            return Response.builder()
                    .orderId(orderDetails.get(0).getOrder().getId())
                    .orderNo(orderDetails.get(0).getOrderNo())
                    .totalPrice(serviceDto.getTotalPrice())
                    .cancellationDate(dateTimeHolder.getUpdateTime(orderDetails.get(0).getOrder()))
                    .refundInfo(RefundInfo.of(serviceDto.getRefundDto()))
                    .cancelledItems(
                            orderDetails.stream()
                                    .map(orderDetail -> ProductInfo.of(orderDetail, orderDetailInfoMap.get(orderDetail.getId())))
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

        public static ProductInfo of(OrderDetail orderDetail, ConfirmCancelReturnDto.OrderDetailInfo orderDetailInfo) {
            return ProductInfo.builder()
                    .productId(orderDetail.getProduct().getId())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .quantity(orderDetailInfo.getNumberOfCancellation())
                    .productNo(orderDetail.getProduct().getProductNo())
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
