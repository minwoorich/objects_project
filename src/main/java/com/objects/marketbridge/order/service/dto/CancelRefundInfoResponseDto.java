package com.objects.marketbridge.order.service.dto;


import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.order.domain.MemberShipPrice.BASIC;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;

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

    public static CancelRefundInfoResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
        if (isBasicMember(memberShip)) {
            return createDto(orderDetails, BASIC.getDeliveryFee(), BASIC.getRefundFee());
        }
        return createDto(orderDetails, WOW.getDeliveryFee(), WOW.getRefundFee());
    }

    private static boolean isBasicMember(String memberShip) {
        return Objects.equals(memberShip, MembershipType.BASIC.getText());
    }

    private static CancelRefundInfoResponseDto createDto(List<OrderDetail> orderDetails, Long deliveryFee, Long refundFee) {
        return CancelRefundInfoResponseDto.builder()
                .discountPrice( // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 -> fetchJoin으로 쿠폰까지 조인후 해결
                        orderDetails.stream()
                                .mapToLong(orderDetail -> orderDetail.getCoupon().getPrice())
                                .sum()
                )
                .totalPrice(
                        orderDetails.stream()
                                .mapToLong(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                                .sum()
                )
                .deliveryFee(deliveryFee)
                .refundFee(refundFee)
                .build();
    }
}
