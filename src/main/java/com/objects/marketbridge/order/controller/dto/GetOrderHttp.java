package com.objects.marketbridge.order.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GetOrderHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime createdAt;
        private Long orderId;
        private List<OrderDetailInfo> orderDetailInfo;
    }

    @Getter
    @NoArgsConstructor
    private static class OrderDetailInfo{
        private Long orderDetailId;
        private Long productId;
        private Long quantity;
        private Long price;
        private Long statusCode;
        private LocalDateTime deliveredDate;
        private String productThumbImageUrl;
        private String productName;
        private Boolean isOwn;
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String keyword;
        private String year;
        private Boolean isSearch;
        private Long memberId;

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }
    }

}
