package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReturnResponseDto {
    private List<ProductInfoResponseDto> productInfoResponseDtos;
    private ReturnRefundInfoResponseDto returnRefundInfoResponseDto;

    @Builder
    private ReturnResponseDto(List<ProductInfoResponseDto> productInfoResponseDtos, ReturnRefundInfoResponseDto returnRefundInfoResponseDto) {
        this.productInfoResponseDtos = productInfoResponseDtos;
        this.returnRefundInfoResponseDto = returnRefundInfoResponseDto;
    }

    public static ReturnResponseDto of(List<OrderDetail> orderDetails, String memberType) {
        return ReturnResponseDto.builder()
                .productInfoResponseDtos(
                        orderDetails.stream()
                        .map(ProductInfoResponseDto::of)
                        .toList()
                )
                .returnRefundInfoResponseDto(
                        ReturnRefundInfoResponseDto.of(orderDetails, memberType)
                )
                .build();
    }
}
