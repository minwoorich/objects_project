package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CancelRefundInfoResponseDtoTest {

    @Test
    @DisplayName("멤버십이 일반이라면 배송비 3000원, 환불비 1000원이다.")
    public void of1() {
        // given
        String memberShip = Membership.BASIC.getText();

        Coupon coupon1 = Coupon.builder()
                .price(1000L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .coupon(coupon1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .coupon(coupon2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        CancelRefundInfoResponseDto result = CancelRefundInfoResponseDto.of(orderDetails, memberShip);

        // then
        assertThat(result)
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(3000L, 1000L, 3000L, 6000L);
    }

    @Test
    @DisplayName("멤버십이 와우라면 배송비, 환불비는 0원이다.")
    public void of2() {
        // given
        String memberShip = Membership.WOW.getText();

        Coupon coupon1 = Coupon.builder()
                .price(1000L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .coupon(coupon1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .coupon(coupon2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        CancelRefundInfoResponseDto result = CancelRefundInfoResponseDto.of(orderDetails, memberShip);

        // then
        assertThat(result)
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(0L, 0L, 3000L, 6000L);
    }

}