package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ServiceDto {

    private Long totalPrice;
    private List<OrderDetail> orderDetails;
    private RefundDto refundDto;
    private Map<Long, OrderDetail> orderDetailMap;
    private Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfoMap;

    @Builder
    private ServiceDto(Long totalPrice, List<OrderDetail> orderDetails, RefundDto refundDto, Map<Long, OrderDetail> orderDetailMap, Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfoMap) {
        this.totalPrice = totalPrice;
        this.orderDetails = orderDetails;
        this.refundDto = refundDto;
        this.orderDetailMap = orderDetailMap;
        this.orderDetailInfoMap = orderDetailInfoMap;
    }

    public static ServiceDto of(Long totalPrice, List<OrderDetail> orderDetails, RefundDto refundDto, Map<Long, OrderDetail> orderDetailMap, Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfoMap) {
        return ServiceDto.builder()
                .totalPrice(totalPrice)
                .orderDetails(orderDetails)
                .refundDto(refundDto)
                .orderDetailMap(orderDetailMap)
                .orderDetailInfoMap(orderDetailInfoMap)
                .build();
    }
}
