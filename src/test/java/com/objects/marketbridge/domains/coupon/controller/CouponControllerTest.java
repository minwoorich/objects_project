package com.objects.marketbridge.domains.coupon.controller;

import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.domains.coupon.controller.dto.GetCouponHttp;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.mock.BaseFakeCouponRepository;
import com.objects.marketbridge.domains.coupon.mock.FakeCouponRepository;
import com.objects.marketbridge.domains.coupon.service.CouponService;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.order.mock.FakeProductRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CouponControllerTest {

    CouponRepository couponRepository = new FakeCouponRepository();
    ProductRepository productRepository = new FakeProductRepository();

    CouponService couponService = CouponService.builder().couponRepository(couponRepository).build();
    CouponController couponController = CouponController.builder().couponService(couponService).build();


    @AfterEach
    void clear() {
        BaseFakeCouponRepository.getInstance().clear();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("상품에 등록된 모든 쿠폰들을 조회할 수 있다.")
    @Test
    void findCouponsForProduct(){
        //given
        Product product = productRepository.save(Product.builder().productNo("111111-111111").name("신발").build());

        Coupon coupon1 = Coupon.builder().price(1000L).product(product).build();
        product.addCoupons(coupon1);
        Coupon coupon2 = Coupon.builder().price(2000L).product(product).build();
        product.addCoupons(coupon2);
        Coupon coupon3 = Coupon.builder().price(3000L).product(product).build();
        product.addCoupons(coupon3);

        couponRepository.saveAll(List.of(coupon1, coupon2, coupon3));

        //when
        ApiResponse<GetCouponHttp.Response> response = couponController.findCouponsForProduct(1L);

        //then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getData().getHasCoupons()).isTrue();
        assertThat(response.getData().getCouponInfos())
                .extracting(c -> c.getProductId(), c->c.getProductNo(), c->c.getCouponPrice())
                .containsExactly(
                        Tuple.tuple(1L, "111111-111111", 1000L),
                        Tuple.tuple(1L, "111111-111111", 2000L),
                        Tuple.tuple(1L, "111111-111111", 3000L)
                );
    }

    @DisplayName("상품에 등록된 쿠폰이 없으면 빈 배열을 반환한다.")
    @Test
    void findCouponsForProduct_empty(){
        //given
        // 등록된 쿠폰이 없음

        //when
        ApiResponse<GetCouponHttp.Response> response = couponController.findCouponsForProduct(1L);

        //then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getData().getHasCoupons()).isFalse();
        assertThat(response.getData().getCouponInfos()).isEmpty();
    }
}