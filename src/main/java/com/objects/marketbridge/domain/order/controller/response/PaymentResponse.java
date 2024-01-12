package com.objects.marketbridge.domain.order.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentResponse {
    private String payType;
    private Long totalOrderPrice;
    private String orderName;
    private String orderNo;
    private String memberName;
    private String email;
    private String successUrl;
    private String failUrl;
    private String failReason;
    private Boolean isCanceled;
    private LocalDateTime createdAt;

    @Builder
    public PaymentResponse(String payType, Long totalOrderPrice, String orderName, String orderNo, String memberName, String email, String successUrl, String failUrl, String failReason, Boolean isCanceled, LocalDateTime createdAt) {
        this.payType = payType;
        this.totalOrderPrice = totalOrderPrice;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.memberName = memberName;
        this.email = email;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.failReason = failReason;
        this.isCanceled = isCanceled;
        this.createdAt = createdAt;
    }

    public static PaymentResponse from(){
        return PaymentResponse.builder().build();
    }
}
