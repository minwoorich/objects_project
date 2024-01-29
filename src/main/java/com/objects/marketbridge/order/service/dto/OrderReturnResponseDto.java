package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderReturnResponseDto {
    private List<ProductInfoResponseDto> productInfoResponseDtos;
    private ReturnRefundInfoResponseDto returnRefundInfoResponseDto;

    @Builder
    private OrderReturnResponseDto(List<ProductInfoResponseDto> productInfoResponseDtos, ReturnRefundInfoResponseDto returnRefundInfoResponseDto) {
        this.productInfoResponseDtos = productInfoResponseDtos;
        this.returnRefundInfoResponseDto = returnRefundInfoResponseDto;
    }

    public static OrderReturnResponseDto of(List<OrderDetail> orderDetails) {
        return OrderReturnResponseDto.builder()
                .productInfoResponseDtos(
                        orderDetails.stream()
                        .map(ProductInfoResponseDto::of)
                        .toList()
                )
                .returnRefundInfoResponseDto(
                        ReturnRefundInfoResponseDto.of(orderDetails)
                )
                .build();

    }
}
