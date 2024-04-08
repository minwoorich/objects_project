package com.objects.marketbridge.domains.coupon.infra;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.coupon.mock.FakeCouponRepository;
import com.objects.marketbridge.domains.order.mock.FakeProductRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CouponRepositoryImplTestWithFake {
    CouponRepository couponRepository = new FakeCouponRepository();
    ProductRepository productRepository = new FakeProductRepository();

    @DisplayName("상품아이디를 통해 상품에 등록된 모든 쿠폰들을 조회할 수 있다.")
    @Test
    void findByProductId(){
        //given
        String productNo = "1번";
        Product product = productRepository.save(Product.builder().productNo(productNo).build());

        Coupon coupon1 = couponRepository.save(Coupon.builder().price(1000L).product(product).build());
        Coupon coupon2 = couponRepository.save(Coupon.builder().price(2000L).product(product).build());
        Coupon coupon3 = couponRepository.save(Coupon.builder().price(3000L).product(product).build());

        product.addCoupons(coupon1);
        product.addCoupons(coupon2);
        product.addCoupons(coupon3);

        //when
        List<Coupon> coupons = couponRepository.findByProductId(product.getId());

        //then
        assertThat(coupons).hasSize(3);
        assertThat(product.getCoupons()).hasSize(3);
        assertThat(product.getCoupons())
                .extracting(Coupon::getId, Coupon::getPrice)
                .containsExactly(
                        tuple(1L, 1000L),
                        tuple(2L, 2000L),
                        tuple(3L, 3000L));

        assertThat(coupons).
                extracting(c -> c.getProduct().getProductNo())
                .containsExactly(productNo, productNo, productNo);
    }
}