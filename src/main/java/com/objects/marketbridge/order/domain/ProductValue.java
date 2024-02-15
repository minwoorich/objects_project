package com.objects.marketbridge.order.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class ProductValue {
    String productNo;
    Long sellerId;
    Long couponId;
    Long quantity;
    String deliveredDate;

    @Builder
    private ProductValue(String productNo, Long sellerId, Long couponId, Long quantity, String deliveredDate) {
        this.productNo = productNo;
        this.sellerId = sellerId;
        this.couponId = couponId;
        this.quantity = quantity;
        this.deliveredDate = deliveredDate;
    }
}
