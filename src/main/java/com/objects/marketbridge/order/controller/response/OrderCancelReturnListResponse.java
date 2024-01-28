package com.objects.marketbridge.order.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCancelReturnListResponse {

    private LocalDateTime cancelReceiptDate;
    private LocalDateTime orderDate;
    private String orderNo;
    private List<OrderDetailResponse> orderDetailResponses;

    @Builder
    @QueryProjection
    public OrderCancelReturnListResponse(LocalDateTime cancelReceiptDate, LocalDateTime orderDate, String orderNo) {
        this.cancelReceiptDate = cancelReceiptDate;
        this.orderDate = orderDate;
        this.orderNo = orderNo;
    }

    public void changeOrderDetailResponseList(List<OrderDetailResponse> orderDetailResponses) {
        this.orderDetailResponses = orderDetailResponses;
    }

}
