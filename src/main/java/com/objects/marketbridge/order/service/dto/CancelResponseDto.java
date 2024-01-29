package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CancelResponseDto {

    private List<ProductInfoResponseDto> productInfoResponseDtos;
    private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

    @Builder
    private CancelResponseDto(List<ProductInfoResponseDto> productInfoResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
        this.productInfoResponseDtos = productInfoResponseDtos;
        this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
    }

    public static CancelResponseDto of(List<OrderDetail> orderDetails, Order order) {
        return CancelResponseDto.builder()
                .productInfoResponseDtos(orderDetails.stream()
                        .map(ProductInfoResponseDto::of)
                        .collect(Collectors.toList())
                )
                .cancelRefundInfoResponseDto(
                        CancelRefundInfoResponseDto.of(orderDetails, order)
                )
                .build();
    }
}
