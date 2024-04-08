package com.objects.marketbridge.domains.coupon.service.dto;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GetCouponDtoTest {

    @DisplayName("Coupon 에서 GetCouponDto 를 생성 할 수 있다.")
    @Test
    void of(){
        //given
        Product product = Product.builder().productNo("productNo1").id(1L).build();
        Coupon coupon = Coupon.builder().product(product).price(1000L).build();

        //when
        GetCouponDto result = GetCouponDto.of(coupon, true);

        //then
        assertThat(result).extracting(c -> c.getPrice(), c-> c.getProductId(), c -> c.getProductNo())
                .containsExactly(1000L, 1L, "productNo1");
    }

}