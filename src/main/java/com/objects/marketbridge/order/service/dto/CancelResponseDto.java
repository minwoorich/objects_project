package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CancelResponseDto {

    private List<ProductInfoResponseDto> productInfoResponseDtos;
    private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

    @Builder
    private CancelResponseDto(List<ProductInfoResponseDto> productInfoResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
        this.productInfoResponseDtos = productInfoResponseDtos;
        this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
    }

    public static CancelResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
        return CancelResponseDto.builder()
                .productInfoResponseDtos(orderDetails.stream()
                        // TODO Product로 인해 N+1 문제 발생 예상 (of)
                        .map(ProductInfoResponseDto::of)
                        .toList()
                )
                .cancelRefundInfoResponseDto(
                        // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 (of) -> fetchJoin으로 쿠폰까지 조인후 해결
                        CancelRefundInfoResponseDto.of(orderDetails, memberShip)
                )
                .build();
    }

}
