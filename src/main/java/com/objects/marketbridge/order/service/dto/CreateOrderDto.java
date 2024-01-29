package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.order.domain.ProductValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderDto {
    private String tid;
    private Long memberId;
    private Long addressId;
    private String orderName;
    private String orderNo;
    private Long totalOrderPrice;
    private List<ProductValue> productValues;

    @Builder
    public CreateOrderDto(String tid, Long memberId, Long addressId, String orderName, String orderNo, Long totalOrderPrice, List<ProductValue> productValues) {
        this.tid = tid;
        this.memberId = memberId;
        this.addressId = addressId;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalOrderPrice = totalOrderPrice;
        this.productValues = productValues;
    }

    public static CreateOrderDto fromRequest(CreateOrderRequest request, Long memberId, String tid) {
        return CreateOrderDto.builder()
                .memberId(memberId)
                .addressId(request.getAddressId())
                .orderName(request.getOrderName())
                .orderNo(request.getOrderId())
                .totalOrderPrice(request.getAmount())
                .productValues(request.getProductValues())
                .tid(tid)
                .build();
    }
}
