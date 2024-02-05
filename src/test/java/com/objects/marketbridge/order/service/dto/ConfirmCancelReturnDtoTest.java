package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ConfirmCancelReturnDtoTest {

    @Test
    @DisplayName("주문, 환불 정보가 주어지면 ConfirmCancelReturnDto.Response를 생성한다.")
    public void response_of() {
        // given
        Order order = Order.builder()
                .orderNo("1")
                .build();
        ReflectionTestUtils.setField(order, "id", 1L, Long.class);

        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product1, "id", 1L, Long.class);
        Product product2 = Product.builder()
                .price(2000L)
                .name("옥지얌")
                .productNo("2")
                .build();
        ReflectionTestUtils.setField(product2, "id", 2L, Long.class);

        Coupon coupon1 = Coupon.builder()
                .price(1000L)
                .product(product1)
                .build();
        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .product(product2)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .product(product1)
                .reason("단순변심")
                .coupon(coupon1)
                .price(1000L)
                .quantity(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .reason("단순변심")
                .product(product2)
                .coupon(coupon2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);

        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 30, 2, 20);
        DateTimeHolder testDateTimeHolder = new TestDateTimeHolder(LocalDateTime.now(), createTime, updateTime, updateTime);

        RefundDto refundDto = RefundDto.builder()
                .refundProcessedAt(updateTime)
                .refundMethod("카드")
                .totalRefundAmount(4000L)
                .build();

        // when
        ConfirmCancelReturnDto.Response result = ConfirmCancelReturnDto.Response.of(order, refundDto, testDateTimeHolder);

        // then
        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
        assertThat(result.getCancellationDate()).isEqualTo(updateTime);

        assertThat(result.getCancelledItems().size()).isEqualTo(2);
        assertThat(result.getCancelledItems().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getCancelledItems().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getCancelledItems().get(0).getName()).isEqualTo("빵빵아");
        assertThat(result.getCancelledItems().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getCancelledItems().get(0).getQuantity()).isEqualTo(1L);

        assertThat(result.getCancelledItems().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getCancelledItems().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getCancelledItems().get(1).getName()).isEqualTo("옥지얌");
        assertThat(result.getCancelledItems().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getCancelledItems().get(1).getQuantity()).isEqualTo(2L);
    }

    @Test
    @DisplayName("상품과 수량이 주어지면 ConfirmCancelReturnDto.ProductInfo를 생성한다.")
    public void productInfo_of_with_product_And_quantity() {
        // given
        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product1, "id", 1L, Long.class);

        Long quantity = 1L;

        // when
        ConfirmCancelReturnDto.ProductInfo result = ConfirmCancelReturnDto.ProductInfo.of(product1, quantity);

        // then
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductNo()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("빵빵아");
        assertThat(result.getPrice()).isEqualTo(1000L);
        assertThat(result.getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("RefundDto가 주어지면 ConfirmCancelReturnDto.RefundInfo를 생성한다.")
    public void refundInfo_of() {
        // given
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 30, 2, 20);

        RefundDto refundDto = RefundDto.builder()
                .refundProcessedAt(updateTime)
                .refundMethod("카드")
                .totalRefundAmount(4000L)
                .build();

        // when
        ConfirmCancelReturnDto.RefundInfo result = ConfirmCancelReturnDto.RefundInfo.of(refundDto);

        // then
        assertThat(result.getRefundMethod()).isEqualTo("카드");
        assertThat(result.getRefundProcessedAt()).isEqualTo(updateTime);
        assertThat(result.getTotalRefundAmount()).isEqualTo(4000L);
    }

}