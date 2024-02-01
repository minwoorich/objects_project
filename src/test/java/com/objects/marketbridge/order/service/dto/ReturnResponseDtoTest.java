package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.objects.marketbridge.common.domain.MembershipType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class ReturnResponseDtoTest {

    @Test
    @DisplayName("일반 맴버십인 경우 배송비 3000원, 반품비 1000원으로 ReturnResponseDto로 변환할 수 있다.")
    public void of1() {
        // given
        String memberShip = BASIC.getText();

        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .thumbImg("빵빵이 이미지")
                .build();
        Product product2 = Product.builder()
                .price(2000L)
                .name("옥지얌")
                .thumbImg("옥지얌 이미지")
                .build();

        Coupon coupon1 = Coupon.builder()
                .price(1000L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .coupon(coupon1)
                .product(product1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .coupon(coupon2)
                .product(product2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        ReturnResponseDto result = ReturnResponseDto.of(orderDetails, memberShip);

        // then
        assertThat(result.getProductInfoResponseDtos()).hasSize(2)
                .extracting("quantity", "name", "price", "image")
                .contains(
                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
                );

        assertThat(result.getReturnRefundInfoResponseDto())
                .extracting("deliveryFee", "returnFee", "productTotalPrice")
                .contains(3000L, 1000L, 6000L);

    }

    @Test
    @DisplayName("일반 맴버십인 경우 배송비 3000원, 반품비 1000원으로 ReturnResponseDto로 변환할 수 있다.")
    public void of2() {
        // given
        String memberShip = WOW.getText();

        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .thumbImg("빵빵이 이미지")
                .build();
        Product product2 = Product.builder()
                .price(2000L)
                .name("옥지얌")
                .thumbImg("옥지얌 이미지")
                .build();

        Coupon coupon1 = Coupon.builder()
                .price(1000L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .coupon(coupon1)
                .product(product1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .coupon(coupon2)
                .product(product2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        ReturnResponseDto result = ReturnResponseDto.of(orderDetails, memberShip);

        // then
        assertThat(result.getProductInfoResponseDtos()).hasSize(2)
                .extracting("quantity", "name", "price", "image")
                .contains(
                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
                );

        assertThat(result.getReturnRefundInfoResponseDto())
                .extracting("deliveryFee", "returnFee", "productTotalPrice")
                .contains(0L, 0L, 6000L);

    }


}