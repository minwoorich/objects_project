package com.objects.marketbridge.domains.coupon.domain;

import com.objects.marketbridge.domains.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class CouponTest {

    @DisplayName("Coupon 을 생성 할 수 있다.")
    @Test
    void create (){
        //given
        String name = "1000원 쿠폰";
        Long price = 1000L;
        Long count = 10L;
        Long minimumPrice = 10000L;
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 12, 0, 0);

        //when
        Coupon coupon = Coupon.create(name, price, count, minimumPrice, startDate, endDate);

        //then
        assertThat(coupon)
                .extracting(
                        Coupon::getName,
                        Coupon::getPrice,
                        Coupon::getCount,
                        Coupon::getMinimumPrice,
                        Coupon::getStartDate,
                        Coupon::getEndDate)
                .contains(name, price, count, minimumPrice, startDate, endDate);
    }
    @DisplayName("ProductNo 의 prefix 만 잘라낼 수 있다")
    @Test
    void parseProductGroupId(){
        //given
        Product product = Product.builder().productNo("111111 - 111111").build();
        Coupon coupon = Coupon.builder().build();

        //when
        product.addCoupons(coupon);

        //then
        assertThat(coupon.getProductGroupId()).isEqualTo(111111L);
    }
}