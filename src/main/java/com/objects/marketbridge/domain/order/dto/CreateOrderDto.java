package com.objects.marketbridge.domain.order.dto;

import com.objects.marketbridge.domain.model.*;
import com.objects.marketbridge.domain.order.controller.request.OrderCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderDto {

    private String statusCode;

    private LocalDateTime deliveredDate;

    private Long prodOrderId;

    private Product product;

    private Coupon coupon;

    private Integer usedCoupon;

    private Integer quantity;

    private Integer price;

    private Integer usedPoint;

    private String reason;

    private LocalDateTime cancelledAt;

    public static CreateOrderDto from(OrderCreateRequest request) {
        return CreateOrderDto.builder()
                .prodOrderId(request.getProductId())
                .build();
    }

}
