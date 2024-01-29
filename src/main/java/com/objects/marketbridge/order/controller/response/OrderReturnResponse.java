package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.service.dto.OrderReturnResponseDto;
import com.objects.marketbridge.order.service.dto.ReturnRefundInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderReturnResponse {

    private List<ProductInfoResponse> productResponses;
    private ReturnRefundInfoResponse returnRefundInfoResponse;

    @Builder
    private OrderReturnResponse(List<ProductInfoResponse> productResponses, ReturnRefundInfoResponse returnRefundInfoResponse) {
        this.productResponses = productResponses;
        this.returnRefundInfoResponse = returnRefundInfoResponse;
    }

    public static OrderReturnResponse of(OrderReturnResponseDto serviceDto) {
        return OrderReturnResponse.builder()
                .productResponses(
                        serviceDto.getProductInfoResponseDtos()
                        .stream()
                        .map(ProductInfoResponse::of)
                        .toList()
                )
                .returnRefundInfoResponse(
                        ReturnRefundInfoResponse.of(serviceDto.getReturnRefundInfoResponseDto())
                )
                .build();

    }
}
