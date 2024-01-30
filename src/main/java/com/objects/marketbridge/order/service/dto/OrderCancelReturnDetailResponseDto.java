package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderCancelReturnDetailResponseDto {

    private LocalDateTime orderDate;
    private LocalDateTime cancelDate;
    private String orderNo;
    private String cancelReason;
    private List<ProductListResponseDto> productListResponseDtos;
    private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

    @Builder
    private OrderCancelReturnDetailResponseDto(LocalDateTime orderDate, LocalDateTime cancelDate, String orderNo, String cancelReason, List<ProductListResponseDto> productListResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
        this.orderDate = orderDate;
        this.cancelDate = cancelDate;
        this.orderNo = orderNo;
        this.cancelReason = cancelReason;
        this.productListResponseDtos = productListResponseDtos;
        this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
    }

    public static OrderCancelReturnDetailResponseDto of(Order order, List<OrderDetail> orderDetails, String memberShip, DateTimeHolder dateTimeHolder) {
        return OrderCancelReturnDetailResponseDto.builder()
                .orderDate(dateTimeHolder.getCreateTime(order))
                .cancelDate(dateTimeHolder.getUpdateTime(order))
                .orderNo(order.getOrderNo())
                .cancelReason(orderDetails.get(0).getReason())
                .productListResponseDtos(
                        orderDetails.stream()
                                .map(ProductListResponseDto::of)
                                .toList()
                )
                .cancelRefundInfoResponseDto(
                        CancelRefundInfoResponseDto.of(orderDetails, memberShip)
                )
                .build();
    }
}
