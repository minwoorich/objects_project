package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.order.domain.MemberShipPrice.*;

@Getter
public class ReturnRefundInfoResponseDto {

    private Long deliveryFee;
    private Long returnFee;
    private Long productTotalPrice;

    @Builder
    private ReturnRefundInfoResponseDto(Long deliveryFee, Long returnFee, Long productTotalPrice) {
        this.deliveryFee = deliveryFee;
        this.returnFee = returnFee;
        this.productTotalPrice = productTotalPrice;
    }

    public static ReturnRefundInfoResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
        if (isBasicMember(memberShip)) {
            return createDto(orderDetails, BASIC.getDeliveryFee(), BASIC.getReturnFee());
        }
        return createDto(orderDetails, WOW.getDeliveryFee(), WOW.getReturnFee());
    }

    private static boolean isBasicMember(String memberShip) {
        return Objects.equals(memberShip, Membership.BASIC.getText());
    }

    private static ReturnRefundInfoResponseDto createDto(List<OrderDetail> orderDetails, Long deliveryFee, Long refundFee) {
        return ReturnRefundInfoResponseDto.builder()
                .deliveryFee(deliveryFee)
                .returnFee(refundFee)
                .productTotalPrice(
                        orderDetails.stream()
                                .mapToLong(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                                .sum()
                )
                .build();
    }
}
