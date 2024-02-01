package com.objects.marketbridge.order.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelRequestDto {

    private String orderNo;
    private String cancelReason;

    @Builder
    private CancelRequestDto(String orderNo, String cancelReason) {
        this.orderNo = orderNo;
        this.cancelReason = cancelReason;
    }
}
