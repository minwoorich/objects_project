package com.objects.marketbridge.order.controller.request;

import com.objects.marketbridge.order.service.dto.CancelRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelRequest {

    @NotNull
    private String orderNo;
    @NotNull
    private String cancelReason;


    @Builder
    public OrderCancelRequest(String orderNo, String cancelReason) {
        this.orderNo = orderNo;
        this.cancelReason = cancelReason;
    }

    public CancelRequestDto toServiceRequest() {
        return CancelRequestDto.builder()
                .orderNo(orderNo)
                .cancelReason(cancelReason)
                .build();
    }
}
