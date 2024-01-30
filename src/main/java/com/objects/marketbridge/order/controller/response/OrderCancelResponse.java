package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.service.dto.CancelResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCancelResponse {

    private List<ProductInfoResponse> productResponses;
    private CancelRefundInfoResponse cancelRefundInfoResponse;

    @Builder
    private OrderCancelResponse(List<ProductInfoResponse> productResponses, CancelRefundInfoResponse cancelRefundInfoResponse) {
        this.productResponses = productResponses;
        this.cancelRefundInfoResponse = cancelRefundInfoResponse;
    }

    public static OrderCancelResponse of(CancelResponseDto serviceDto) {
        return OrderCancelResponse.builder()
                .productResponses(
                        serviceDto.getProductInfoResponseDtos()
                                .stream()
                                .map(ProductInfoResponse::of)
                                .toList()
                )
                .cancelRefundInfoResponse(
                        CancelRefundInfoResponse.of(serviceDto.getCancelRefundInfoResponseDto())
                )
                .build();
    }

}
