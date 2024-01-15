package com.objects.marketbridge.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateProdOrderDto {
    private String paymentMethod;
    private Long memberId;
    private Long addressId;
    private String orderName;
    private Long totalOrderPrice;
    private String orderNo;

    @Builder
    public CreateProdOrderDto(String paymentMethod, Long memberId, Long addressId, String orderName, Long totalOrderPrice, String orderNo) {
        this.paymentMethod = paymentMethod;
        this.memberId = memberId;
        this.addressId = addressId;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalOrderPrice = totalOrderPrice;
    }
}
