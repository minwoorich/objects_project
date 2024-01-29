package com.objects.marketbridge.order.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelRequestDto {

    private Long orderId;
    private String cancelReason;

    @Builder
    private CancelRequestDto(Long orderId, String cancelReason) {
        this.orderId = orderId;
        this.cancelReason = cancelReason;
    }
}
