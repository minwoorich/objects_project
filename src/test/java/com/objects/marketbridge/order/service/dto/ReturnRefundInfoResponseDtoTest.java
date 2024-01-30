package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnRefundInfoResponseDtoTest {

    @Test
    @DisplayName("멤버십이 일반이라면 배송비 3000원, 반품비 1000원이다.")
    public void of1() {
        String memberShip = Membership.BASIC.getText();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        ReturnRefundInfoResponseDto result = ReturnRefundInfoResponseDto.of(orderDetails, memberShip);

        // then
        assertThat(result)
                .extracting("deliveryFee", "returnFee", "productTotalPrice")
                .contains(3000L, 1000L, 6000L);
    }

}