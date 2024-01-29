package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CancelRefundInfoResponseDto {

    private Long deliveryFee;
    private Long refundFee;
    private Long discountPrice;
    private Long totalPrice;

    @Builder
    private CancelRefundInfoResponseDto(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
        this.deliveryFee = deliveryFee;
        this.refundFee = refundFee;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }

    public static CancelRefundInfoResponseDto of(List<OrderDetail> orderDetails, Order order) {
        return CancelRefundInfoResponseDto.builder()
                .refundFee(0L)
                .deliveryFee(0L) // TODO 배송비 가져오기
                .discountPrice(order.getTotalUsedCouponPrice()) // TODO 할인금액 쿠폰만 가능 -> 리팩토링 필요
                .totalPrice(orderDetails.stream()
                        .mapToLong(OrderDetail::getPrice)
                        .sum()
                )
                .build();
    }
}
