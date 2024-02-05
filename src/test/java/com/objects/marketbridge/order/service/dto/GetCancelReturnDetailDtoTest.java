package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.member.domain.MembershipType.BASIC;
import static com.objects.marketbridge.member.domain.MembershipType.WOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class GetCancelReturnDetailDtoTest {


    @Test
    @DisplayName("주문, 주문 상세가 주어지면 Response로 변환할 수 있다.")
    public void response_of_WOW() {
        // given
        String memberShip = WOW.getText();

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
        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(LocalDateTime.now(), createTime, updateTime, updateTime);

        // when
        GetCancelReturnDetailDto.Response result = GetCancelReturnDetailDto.Response.of(order, orderDetails, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, updateTime, "123", "단순변심");

        assertThat(result.getProductInfos())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(
                        tuple(1L, "1", "빵빵아", 1000L, 1L),
                        tuple(2L, "2", "옥지얌", 2000L, 2L)
                );

        assertThat(result.getCancelRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.WOW.getDeliveryFee(), MemberShipPrice.WOW.getRefundFee(), 3000L, 5000L);

    }

    @Test
    @DisplayName("주문, 주문 상세가 주어지면 Response로 변환할 수 있다.")
    public void response_of_BASIC() {
        // given
        String memberShip = BASIC.getText();

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
        DateTimeHolder testDateTimeHolder = new TestDateTimeHolder(LocalDateTime.now(), createTime, updateTime, updateTime);

        // when
        GetCancelReturnDetailDto.Response result = GetCancelReturnDetailDto.Response.of(order, orderDetails, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, updateTime, "123", "단순변심");

        assertThat(result.getProductInfos())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(
                        tuple(1L, "1", "빵빵아", 1000L, 1L),
                        tuple(2L, "2", "옥지얌", 2000L, 2L)
                );

        assertThat(result.getCancelRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.BASIC.getDeliveryFee(), MemberShipPrice.BASIC.getRefundFee(), 3000L, 5000L);

    }

    @Test
    @DisplayName("주문 상세가 주어지면 ProductInfo를 반환한다.")
    public void productInfo_of_With_OrderDetail() {
        Product product = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .productNo("123")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);
        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .price(product.getPrice())
                .quantity(2L)
                .build();

        // when
        GetCancelReturnDetailDto.ProductInfo result = GetCancelReturnDetailDto.ProductInfo.of(orderDetail);

        // then
        assertThat(result).extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "123", "빵빵이", 1000L, 2L);
    }


    @Test
    @DisplayName("상품과 수량이 주어졌을 때 ProductInfo를 반환한다.")
    public void productInfo_of_With_Product_And_Quantity() {
        Long quantity = 2L;

        Product product = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .productNo("123")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        // when
        GetCancelReturnDetailDto.ProductInfo result = GetCancelReturnDetailDto.ProductInfo.of(product, quantity);

        // then
        assertThat(result).extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "123", "빵빵이", 1000L, 2L);

    }

    @Test
    @DisplayName("주문상세, 멤버십이 주어지면 CancelRefundInfo를 반환한다.(BASIC)")
    public void cancelRefundInfo_of_BASIC() {
        // given
        String memberShip = BASIC.getText();

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
                .price(1000L)
                .quantity(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .reason("단순변심")
                .product(product2)
                .coupon(coupon2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        GetCancelReturnDetailDto.CancelRefundInfo result = GetCancelReturnDetailDto.CancelRefundInfo.of(orderDetails, memberShip);

        // then
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
        assertThat(result.getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
    }

    @Test
    @DisplayName("주문상세, 멤버십이 주어지면 CancelRefundInfo를 반환한다.(WOW)")
    public void cancelRefundInfo_of_WOW() {
        // given
        String memberShip = WOW.getText();

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
                .price(1000L)
                .quantity(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .reason("단순변심")
                .product(product2)
                .coupon(coupon2)
                .price(2000L)
                .quantity(2L)
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        // when
        GetCancelReturnDetailDto.CancelRefundInfo result = GetCancelReturnDetailDto.CancelRefundInfo.of(orderDetails, memberShip);

        // then
        assertThat(result.getTotalPrice()).isEqualTo(5000L);
        assertThat(result.getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
    }

}