package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestCancelDtoTest {

    @Test
    @DisplayName("일반 맴버십인 경우 배송비 3000원, 반품비 1000원으로 Response 변환할 수 있다.")
    public void response_of_BASIC() {
        // given
        String memberShip = MembershipType.BASIC.getText();

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

        OrderDetail orderDetail1 = OrderDetail.builder()
                .product(product1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .product(product2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        RequestCancelDto.Response result = RequestCancelDto.Response.of(orderDetails, memberShip);

        // then
        assertThat(result.getProductInfos()).hasSize(2)
                .extracting("quantity", "name", "price", "image")
                .contains(
                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
                );

        assertThat(result.getCancelRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(3000L, 1000L, 3000L, 6000L);

    }

    @Test
    @DisplayName("와우 맴버십인 경우 배송비 0원, 반품비 0원으로 Response로 변환할 수 있다.")
    public void response_of_WOW() {
        // given
        String memberShip = MembershipType.WOW.getText();

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

        OrderDetail orderDetail1 = OrderDetail.builder()
                .product(product1)
                .price(1000L)
                .quantity(2L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .product(product2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        RequestCancelDto.Response result = RequestCancelDto.Response.of(orderDetails, memberShip);

        // then
        assertThat(result.getProductInfos()).hasSize(2)
                .extracting("quantity", "name", "price", "image")
                .contains(
                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
                );

        assertThat(result.getCancelRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(0L, 0L, 3000L, 6000L);
    }

    @Test
    @DisplayName("주문 상세를 ProductInfo로 변환")
    public void productInfo_of() {
        // given
        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .thumbImg("빵빵이 이미지")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .product(product1)
                .price(product1.getPrice())
                .quantity(2L)
                .build();

        // when
        RequestCancelDto.ProductInfo result = RequestCancelDto.ProductInfo.of(orderDetail);

        // then
        assertThat(result).extracting("quantity", "name", "price", "image")
                .contains(2L, "빵빵이", 1000L, "빵빵이 이미지");
    }

    @Test
    @DisplayName("주문 상세 리스트와 멤버십이 주어지면 CancelRefundInfo를 반한다.(BASIC)")
    public void CancelRefundInfo_of_BASIC() {
        // given
        String memberShip = MembershipType.BASIC.getText();

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
        RequestCancelDto.CancelRefundInfo result = RequestCancelDto.CancelRefundInfo.of(orderDetails, memberShip);

        // then
        assertThat(result)
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(3000L, 1000L, 3000L, 6000L);
    }

    @Test
    @DisplayName("주문 상세 리스트와 멤버십이 주어지면 CancelRefundInfo를 반한다.(WOW)")
    public void CancelRefundInfo_of_WOW() {
        // given
        String memberShip = MembershipType.WOW.getText();

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
        RequestCancelDto.CancelRefundInfo result = RequestCancelDto.CancelRefundInfo.of(orderDetails, memberShip);

        // then
        assertThat(result)
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(0L, 0L, 3000L, 6000L);
    }

}