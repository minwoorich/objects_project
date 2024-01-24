package com.objects.marketbridge.domain.order.dto;

import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderDto {
    private Long memberId;
    private Long addressId;
    private String orderName;
    private String orderNo;
    private Long totalOrderPrice;
    private Long realOrderPrice;
    private List<ProductValue> productValues;

    @Builder
    public CreateOrderDto(Long memberId, Long addressId, String orderName, String orderNo, Long totalOrderPrice, Long realOrderPrice,   List<ProductValue> productValues) {
        this.memberId = memberId;
        this.addressId = addressId;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalOrderPrice = totalOrderPrice;
        this.realOrderPrice = realOrderPrice;
        this.productValues = productValues;
    }

    public static CreateOrderDto fromRequest(CreateOrderRequest request, Long memberId) {
        return CreateOrderDto.builder()
                .memberId(memberId)
                .addressId(request.getAddressId())
                .orderName(request.getOrderName())
                .orderNo(request.getOrderId())
                .totalOrderPrice(request.getAmount())
                .productValues(request.getProductValues())
                .build();
    }

    public CreateOrderResponse toResponse(String email, String successUrl, String failUrl) {
        return CreateOrderResponse.builder()
                .totalOrderPrice(totalOrderPrice)
                .orderName(orderName)
                .orderNo(orderNo)
                .email(email)
                .successUrl(successUrl)
                .failUrl(failUrl)
                .build();
    }
}
