package com.objects.marketbridge.domains.coupon.infra;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
@ActiveProfiles("test")
class CouponJpaRepositoryTest {

    @Autowired ProductRepository productRepository;
    @Autowired CouponRepository couponRepository;

    @DisplayName("상품에 등록된 모든 쿠폰들을 조회할 수 있다.")
    @Test
    void findByProductId(){
        //given
        String productNo = "1번";
        Product product = Product.builder().productNo(productNo).build();
        productRepository.save(product);

        Coupon coupon1 = Coupon.builder().price(1000L).product(product).build();
        Coupon coupon2 = Coupon.builder().price(2000L).product(product).build();
        Coupon coupon3 = Coupon.builder().price(3000L).product(product).build();
        couponRepository.saveAll(List.of(coupon1, coupon2, coupon3));

        //when
        List<Coupon> coupons = couponRepository.findByProductId(product.getId());

        //then
        assertThat(coupons).hasSize(3);
    }

    @DisplayName("상품에 등록된 쿠폰이 없을 경우 빈 List 객체를 반환한다.")
    @Test
    void findByProductId_empty(){
        //given
        // 아무런 쿠폰도 저장하지 않은 상태

        //when
        List<Coupon> coupons = couponRepository.findByProductId(9L);

        //then
        assertThat(coupons).isNotNull();
        assertThat(coupons).isInstanceOf(List.class);
        assertThat(coupons).isEmpty();
    }

    @DisplayName("상품그룹에 등록된 모든 쿠폰들을 조회할 수 있다.")
    @Test
    void findByProductGroupId(){
        //given
        String productNo = "111111 - 111111";
        Product product = Product.builder().productNo(productNo).build();
        productRepository.save(product);

        Coupon coupon1 = Coupon.builder().price(1000L).count(100L).product(product).build();
        Coupon coupon2 = Coupon.builder().price(2000L).count(100L).product(product).build();
        Coupon coupon3 = Coupon.builder().price(3000L).count(100L).product(product).build();
        couponRepository.saveAll(List.of(coupon1, coupon2, coupon3));

        //when
        List<Coupon> coupons = couponRepository.findByProductId(product.getId());

        //then
        assertThat(coupons).hasSize(3);
    }
}