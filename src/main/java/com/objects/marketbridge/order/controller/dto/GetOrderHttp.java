package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.infra.dtio.OrderDetailDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GetOrderHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        List<OrderInfo> orderInfos;

        @Builder
        private Response(List<OrderInfo> orderInfos) {
            this.orderInfos = orderInfos;
        }

        public static Response of(List<OrderDtio> orderDtios) {
            return Response.builder()
                    .orderInfos(orderDtios.stream().map(OrderInfo::of).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    private static class OrderInfo{
        private String createdAt;
        private String orderNo;
        private List<OrderDetailInfo> orderDetailInfos;

        @Builder
        private OrderInfo(String createdAt, String orderNo, List<OrderDetailInfo> orderDetailInfos) {
            this.createdAt = createdAt;
            this.orderNo = orderNo;
            this.orderDetailInfos = orderDetailInfos;
        }

        public static OrderInfo of(OrderDtio orderDtio) {
            return OrderInfo.builder()
                    .createdAt(orderDtio.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .orderNo(orderDtio.getOrderNo())
                    .orderDetailInfos(orderDtio.getOrderDetails().stream().map(OrderDetailInfo::of).collect(Collectors.toList()))
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor
    private static class OrderDetailInfo{
        private Long orderDetailId;
        private Long productId;
        private Long quantity;
        private Long price;
        private String statusCode;
        private LocalDateTime deliveredDate;
        private String productThumbImageUrl;
        private String productName;
        private Boolean isOwn;

        @Builder
        private OrderDetailInfo(Long orderDetailId, Long productId, Long quantity, Long price, String statusCode, LocalDateTime deliveredDate, String productThumbImageUrl, String productName, Boolean isOwn) {
            this.orderDetailId = orderDetailId;
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
            this.statusCode = statusCode;
            this.deliveredDate = deliveredDate;
            this.productThumbImageUrl = productThumbImageUrl;
            this.productName = productName;
            this.isOwn = isOwn;
        }

        public static OrderDetailInfo of(OrderDetailDtio orderDetailDtio) {
            return OrderDetailInfo.builder()
                    .orderDetailId(orderDetailDtio.getOrderDetailId())
                    .productId(orderDetailDtio.getProduct().getProductId())
                    .quantity(orderDetailDtio.getQuantity())
                    .price(orderDetailDtio.getPrice())
                    .statusCode(orderDetailDtio.getStatusCode())
                    .deliveredDate(orderDetailDtio.getDeliveredDate())
                    .productThumbImageUrl(orderDetailDtio.getProduct().getThumbImg())
                    .productName(orderDetailDtio.getProduct().getName())
                    .isOwn(orderDetailDtio.getProduct().getIsOwn())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String keyword;
        private String year;
        private Long memberId;
        private Boolean isSearch;

        @Builder
        public Condition(String keyword, String year, Long memberId, Boolean isSearch) {
            this.keyword = keyword;
            this.year = year;
            this.memberId = memberId;
            this.isSearch = isSearch;
        }
    }

}
