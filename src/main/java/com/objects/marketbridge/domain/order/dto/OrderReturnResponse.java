package com.objects.marketbridge.domain.order.dto;

import com.objects.marketbridge.domain.order.controller.response.ProductInfoResponse;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public static OrderReturnResponse of(List<OrderDetail> orderDetails) {
        return OrderReturnResponse.builder()
                .productResponses(orderDetails.stream()
                        .map(ProductInfoResponse::of)
                        .collect(Collectors.toList())
                )
                .returnRefundInfoResponse(
                        ReturnRefundInfoResponse.builder()
                                .productPrice(
                                        orderDetails.stream()
                                                .mapToLong(OrderDetail::getPrice)
                                                .sum()
                                )
                                .deliveryFee(0L)
                                .returnFee(0L)
                                .build()
                )
                .build();

    }
}
