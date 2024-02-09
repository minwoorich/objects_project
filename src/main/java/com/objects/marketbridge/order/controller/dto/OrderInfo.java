package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.infra.dtio.OrderDetailDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderInfo{
    private String createdAt;
    private String orderNo;
    private List<OrderDetailInfo> orderDetailInfos;

    @Builder
    private OrderInfo(String createdAt, String orderNo, List<OrderDetailInfo> orderDetailInfos) {
        this.createdAt = createdAt;
        this.orderNo = orderNo;
        this.orderDetailInfos = orderDetailInfos;
    }

    public static OrderInfo of(OrderDtio orderDtio) {
        return OrderInfo.builder()
                .createdAt(orderDtio.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .orderNo(orderDtio.getOrderNo())
                .orderDetailInfos(orderDtio.getOrderDetails().stream().map(OrderDetailInfo::of).collect(Collectors.toList()))
                .build();
    }

    public static OrderInfo create(String createdAt, String orderNo, List<OrderDetailInfo> orderDetailInfos) {
        return OrderInfo.builder()
                .createdAt(createdAt)
                .orderNo(orderNo)
                .orderDetailInfos(orderDetailInfos)
                .build();
    }


}
