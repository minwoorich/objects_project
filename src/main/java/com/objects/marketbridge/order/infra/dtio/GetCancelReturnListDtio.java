package com.objects.marketbridge.order.infra.dtio;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetCancelReturnListDtio {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime cancelReceiptDate;
        private LocalDateTime orderDate;
        private String orderNo;
        private List<OrderDetailInfo> orderDetailInfos = new ArrayList<>();

        @Builder
        @QueryProjection
        public Response(LocalDateTime cancelReceiptDate, LocalDateTime orderDate, String orderNo) {
            this.cancelReceiptDate = cancelReceiptDate;
            this.orderDate = orderDate;
            this.orderNo = orderNo;
        }

        public void changeOrderDetailInfos(List<OrderDetailInfo> orderDetailInfos) {
            if (orderDetailInfos == null) {
                throw new IllegalArgumentException("주어진 주문 상세 리스트가 존재하지 않습니다.");
            }
            this.orderDetailInfos = orderDetailInfos;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class OrderDetailInfo {

        private String orderNo;
        private Long productId;
        private String productNo;
        private String name;
        private Long price;
        private Long quantity;
        private String orderStatus;

        @Builder
        @QueryProjection
        public OrderDetailInfo(String orderNo, Long productId, String productNo, String name, Long price, Long quantity, String orderStatus) {
            this.orderNo = orderNo;
            this.productId = productId;
            this.productNo = productNo;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.orderStatus = orderStatus;
        }
    }

}
