package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.service.dto.OrderCancelReturnDetailResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCancelReturnDetailResponse {

    private LocalDateTime orderDate;
    private LocalDateTime cancelDate;
    private String orderNo;
    private String cancelReason;
    private List<ProductResponse> productResponseList;
    private CancelRefundInfoResponse cancelRefundInfoResponse;


    @Builder
    private OrderCancelReturnDetailResponse(LocalDateTime orderDate, String orderNo, List<ProductResponse> productResponseList, LocalDateTime cancelDate, String cancelReason, CancelRefundInfoResponse cancelRefundInfoResponse) {
        this.orderDate = orderDate;
        this.orderNo = orderNo;
        this.productResponseList = productResponseList;
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
        this.cancelRefundInfoResponse = cancelRefundInfoResponse;
    }

    public static OrderCancelReturnDetailResponse of(OrderCancelReturnDetailResponseDto serviceDto) {
        return OrderCancelReturnDetailResponse.builder()
                .orderDate(serviceDto.getOrderDate())
                .cancelDate(serviceDto.getCancelDate())
                .orderNo(serviceDto.getOrderNo())
                .cancelReason(serviceDto.getCancelReason())
                .cancelRefundInfoResponse(CancelRefundInfoResponse.of(serviceDto.getCancelRefundInfoResponseDto()))
                .build();
    }

}
