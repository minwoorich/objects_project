package com.objects.marketbridge.domain.order.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReturnRefundInfoResponse {

    private Long deliveryFee;
    private Long returnFee;
    private Long productPrice;

    @Builder
    private ReturnRefundInfoResponse(Long deliveryFee, Long returnFee, Long productPrice) {
        this.deliveryFee = deliveryFee;
        this.returnFee = returnFee;
        this.productPrice = productPrice;
    }
}
