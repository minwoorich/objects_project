package com.objects.marketbridge.domain.order.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderResponse {
    private Long totalOrderPrice;
    private String orderName;
    private String orderNo;
    private String email;
    private String successUrl;
    private String failUrl;


    @Builder
    public CreateOrderResponse(Long totalOrderPrice, String orderName, String orderNo, String email, String successUrl, String failUrl) {
        this.totalOrderPrice = totalOrderPrice;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.email = email;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
    }
}
