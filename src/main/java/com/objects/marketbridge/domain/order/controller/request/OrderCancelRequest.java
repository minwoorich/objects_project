package com.objects.marketbridge.domain.order.controller.request;

import com.objects.marketbridge.domain.order.dto.OrderCancelServiceDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private String reason;

    @Builder
    public OrderCancelRequest(Long orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public OrderCancelServiceDto toServiceRequest() {
        return OrderCancelServiceDto.builder()
                .orderId(orderId)
                .reason(reason)
                .build();
    }
}
