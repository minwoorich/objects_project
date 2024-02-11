package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.objects.marketbridge.member.domain.MembershipType.BASIC;
import static com.objects.marketbridge.member.domain.MembershipType.WOW;
import static org.assertj.core.api.Assertions.assertThat;

class GetCancelDetailDtoTest {


    @Test
    @DisplayName("Response를 반환한다. (WOW_NoCoupon)")
    public void response_of_WOW_NoCoupon() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 30, 2, 20);

        String memberShip = WOW.getText();

        Product product = Product.builder()
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .orderNo("1")
                .product(product)
                .reason("단순변심")
                .price(1000L)
                .quantity(1L)
                .cancelledAt(cancelledAt)
                .build();

        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(null, createTime, null, null);

        // when
        GetCancelDetailDto.Response result = GetCancelDetailDto.Response.of(orderDetail, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, cancelledAt, "1", "단순변심");

        assertThat(result.getProductInfo())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "1", "빵빵아", 1000L, 1L);

        assertThat(result.getRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.WOW.getDeliveryFee(), MemberShipPrice.WOW.getRefundFee(), 0L, 1000L);
    }

    @Test
    @DisplayName("Response를 반환한다. (BASIC_NoCoupon)")
    public void response_of_BASIC_NoCoupon() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 30, 2, 20);

        String memberShip = BASIC.getText();

        Product product = Product.builder()
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .orderNo("1")
                .product(product)
                .reason("단순변심")
                .price(1000L)
                .quantity(1L)
                .cancelledAt(cancelledAt)
                .build();

        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(null, createTime, null, null);

        // when
        GetCancelDetailDto.Response result = GetCancelDetailDto.Response.of(orderDetail, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, cancelledAt, "1", "단순변심");

        assertThat(result.getProductInfo())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "1", "빵빵아", 1000L, 1L);

        assertThat(result.getRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.BASIC.getDeliveryFee(), MemberShipPrice.BASIC.getRefundFee(), 0L, 1000L);
    }

    @Test
    @DisplayName("Response를 반환한다. (WOW_Coupon)")
    public void response_of_WOW_Coupon() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 30, 2, 20);

        String memberShip = WOW.getText();

        Product product = Product.builder()
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .memberCoupon(memberCoupon)
                .orderNo("1")
                .product(product)
                .reason("단순변심")
                .price(1000L)
                .quantity(1L)
                .cancelledAt(cancelledAt)
                .build();

        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(null, createTime, null, null);

        // when
        GetCancelDetailDto.Response result = GetCancelDetailDto.Response.of(orderDetail, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, cancelledAt, "1", "단순변심");

        assertThat(result.getProductInfo())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "1", "빵빵아", 1000L, 1L);

        assertThat(result.getRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.WOW.getDeliveryFee(), MemberShipPrice.WOW.getRefundFee(), 1000L, 1000L);
    }

    @Test
    @DisplayName("Response를 반환한다. (BASIC_Coupon)")
    public void response_of_BASIC_Coupon() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2024, 1, 30, 2, 15);
        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 30, 2, 20);

        String memberShip = BASIC.getText();

        Product product = Product.builder()
                .name("빵빵아")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .memberCoupon(memberCoupon)
                .orderNo("1")
                .product(product)
                .reason("단순변심")
                .price(1000L)
                .quantity(1L)
                .cancelledAt(cancelledAt)
                .build();

        TestDateTimeHolder testDateTimeHolder = new TestDateTimeHolder(null, createTime, null, null);

        // when
        GetCancelDetailDto.Response result = GetCancelDetailDto.Response.of(orderDetail, memberShip, testDateTimeHolder);

        // then
        assertThat(result).extracting("orderDate", "cancelDate", "orderNo", "cancelReason")
                .contains(createTime, cancelledAt, "1", "단순변심");

        assertThat(result.getProductInfo())
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "1", "빵빵아", 1000L, 1L);

        assertThat(result.getRefundInfo())
                .extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
                .contains(MemberShipPrice.BASIC.getDeliveryFee(), MemberShipPrice.BASIC.getRefundFee(), 1000L, 1000L);
    }


    @Test
    @DisplayName("ProductInfo를 반환한다.")
    public void productInfo_of_With_OrderDetail() {
        Product product = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .productNo("1")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .product(product)
                .price(product.getPrice())
                .quantity(2L)
                .build();

        // when
        GetCancelDetailDto.ProductInfo result = GetCancelDetailDto.ProductInfo.of(orderDetail);

        // then
        assertThat(result).extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "1", "빵빵이", 1000L, 2L);
    }

    @Test
    @DisplayName("CancelRefundInfo를 반환한다.(BASIC_Coupon)")
    public void cancelRefundInfo_of_BASIC_Coupon() {
        // given
        String memberShip = BASIC.getText();

        Coupon coupon = Coupon.builder()
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .memberCoupon(memberCoupon)
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        GetCancelDetailDto.RefundInfo result = GetCancelDetailDto.RefundInfo.of(orderDetail, memberShip);

        // then
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getTotalPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("CancelRefundInfo를 반환한다.(WOW_Coupon)")
    public void cancelRefundInfo_of_WOW_Coupon() {
        // given
        String memberShip = WOW.getText();

        Coupon coupon = Coupon.builder()
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .memberCoupon(memberCoupon)
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        GetCancelDetailDto.RefundInfo result = GetCancelDetailDto.RefundInfo.of(orderDetail, memberShip);

        // then
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(result.getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getTotalPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("CancelRefundInfo를 반환한다.(BASIC_NoCoupon)")
    public void cancelRefundInfo_of_BASIC_NoCoupon() {
        // given
        String memberShip = BASIC.getText();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        GetCancelDetailDto.RefundInfo result = GetCancelDetailDto.RefundInfo.of(orderDetail, memberShip);

        // then
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getTotalPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("CancelRefundInfo를 반환한다.(WOW_NoCoupon)")
    public void cancelRefundInfo_of_WOW_NoCoupon() {
        // given
        String memberShip = WOW.getText();

        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(0L)
                .price(1000L)
                .quantity(1L)
                .build();

        // when
        GetCancelDetailDto.RefundInfo result = GetCancelDetailDto.RefundInfo.of(orderDetail, memberShip);

        // then
        assertThat(result.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(result.getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getTotalPrice()).isEqualTo(1000L);
    }

}