package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.order.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDto {

    private Long memberId;
    private AddressValue address;
    private String orderName;
    private String orderNo;
    private Long totalDiscount; // 총 할인 금액 (쿠폰,포인트,멤버쉽)
    private Long totalPrice; // 총 금액
    private Long realPrice; // 실 결제 금액

    private final List<OrderDetailDto> orderDetails = new ArrayList<>();

    @Builder
    private OrderDto(Long memberId, AddressValue address, String orderName, String orderNo, Long totalDiscount, Long totalPrice, Long realPrice) {
        this.memberId = memberId;
        this.address = address;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalDiscount = totalDiscount;
        this.totalPrice = totalPrice;
        this.realPrice = realPrice;
    }

    public static OrderDto of(Order order) {
        return OrderDto.builder()
                .memberId(order.getMember().getId())
                .address(order.getAddress().getAddressValue())
                .orderName(order.getOrderName())
                .orderNo(order.getOrderNo())
                .totalDiscount(order.getTotalDiscount())
                .totalPrice(order.getTotalPrice())
                .realPrice(order.getRealPrice())
                .build();

    }
}
