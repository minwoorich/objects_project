package com.objects.marketbridge.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrderDto {

    private String statusCode;

    private String payMethod;

    private LocalDateTime deliveredDate;

    private String orderName;

    private Long prodOrderId;

    private Long totalPrice; // 총 주문 금액

    private Integer usedPoint;

    private LocalDateTime cancelledAt;

    private List<ProductInfoDto> productInfos;

    @Builder
    public CreateOrderDto(String statusCode, String payMethod, LocalDateTime deliveredDate, String orderName, Long prodOrderId, Integer usedPoint, LocalDateTime cancelledAt, @Singular("productInfo") List<ProductInfoDto> productInfos, Long productId1, Integer amount, Long productOptionId1, Long usedCouponId) {
        this.statusCode = statusCode;
        this.payMethod = payMethod;
        this.deliveredDate = deliveredDate;
        this.orderName = orderName;
        this.prodOrderId = prodOrderId;
        this.usedPoint = usedPoint;
        this.cancelledAt = cancelledAt;
        this.productInfos = productInfos;
    }
}
