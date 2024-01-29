package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReturnRefundInfoResponseDto {

    private Long deliveryFee;
    private Long returnFee;
    private Long productPrice;

    @Builder
    private ReturnRefundInfoResponseDto(Long deliveryFee, Long returnFee, Long productPrice) {
        this.deliveryFee = deliveryFee;
        this.returnFee = returnFee;
        this.productPrice = productPrice;
    }

    public static ReturnRefundInfoResponseDto of(List<OrderDetail> orderDetails) {
        return ReturnRefundInfoResponseDto.builder()
                .deliveryFee(0L)
                .returnFee(0L)
                .productPrice(
                        orderDetails.stream()
                        .mapToLong(OrderDetail::getPrice)
                        .sum()
                )
                .build();
    }
}
