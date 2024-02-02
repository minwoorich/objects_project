package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;


import static com.objects.marketbridge.common.domain.MembershipType.BASIC;
import static com.objects.marketbridge.common.domain.MembershipType.WOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class OrderCancelReturnDetailResponseDtoTest {

    @Test
    @DisplayName("주문, 주문 상세가 주어지면 OrderCancelReturnDetailResponseDto로 변환할 수 있다.")
    public void ofWithWOW() {
        // given
        Order order = Order.builder()
                .orderNo("123")
                .build();

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
                .product(product1)
                .reason("단순변심")
                .coupon(coupon1)
                .price(product1.getPrice())
                .quantity(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .reason("단순변심")
                .product(product2)
                .coupon(coupon2)
                .price(product2.getPrice())
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 30, 2, 20);
//        DateTimeHolder timeHolder = TestDateTimeHolder.builder()
//                .createTime(createTime)
//                .updateTime(updateTime)
//                .build();

        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(LocalDateTime.now(), createTime, updateTime, updateTime);

        // when
        OrderCancelReturnDetailResponseDto result = OrderCancelReturnDetailResponseDto.of(order, orderDetails, WOW.getText(), testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, updateTime, "123", "단순변심");

        assertThat(result.getProductListResponseDtos())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(
                        tuple(1L, "1", "빵빵아", 1000L, 1L),
                        tuple(2L, "2", "옥지얌", 2000L, 2L)
                );

        assertThat(result.getCancelRefundInfoResponseDto())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.WOW.getDeliveryFee(), MemberShipPrice.WOW.getRefundFee(), 3000L, 5000L);

    }

    @Test
    @DisplayName("주문, 주문 상세가 주어지면 OrderCancelReturnDetailResponseDto로 변환할 수 있다.")
    public void ofWithBasic() {
        // given
        Order order = Order.builder()
                .orderNo("123")
                .build();

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
                .product(product1)
                .reason("단순변심")
                .coupon(coupon1)
                .price(product1.getPrice())
                .quantity(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .reason("단순변심")
                .product(product2)
                .coupon(coupon2)
                .price(product2.getPrice())
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 30, 2, 20);
//        TestDateTimeHolder timeHolder = TestDateTimeHolder.builder()
//                .createTime(createTime)
//                .updateTime(updateTime)
//                .build();
        DateTimeHolder testDateTimeHolder = new TestDateTimeHolder(LocalDateTime.now(), createTime, updateTime, updateTime);

        // when
        OrderCancelReturnDetailResponseDto result = OrderCancelReturnDetailResponseDto.of(order, orderDetails, BASIC.getText(), testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, updateTime, "123", "단순변심");

        assertThat(result.getProductListResponseDtos())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(
                        tuple(1L, "1", "빵빵아", 1000L, 1L),
                        tuple(2L, "2", "옥지얌", 2000L, 2L)
                );

        assertThat(result.getCancelRefundInfoResponseDto())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.BASIC.getDeliveryFee(), MemberShipPrice.BASIC.getRefundFee(), 3000L, 5000L);

    }

}