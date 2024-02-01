package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ProductListResponseDtoTest {


    @Test
    @DisplayName("주문 상세가 주어졌을 때 ProductListResponseDto 변환")
    public void ofWithOrderDetail() {
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
        ProductListResponseDto result = ProductListResponseDto.of(orderDetail);

        // then
        assertThat(result).extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "123", "빵빵이", 1000L, 2L);
    }


    @Test
    @DisplayName("상품과 수량이 주어졌을 때 ProductListResponseDto 변환")
    public void ofWithProductAndQuantity() {
        Long quantity = 2L;

        Product product = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .productNo("123")
                .build();
        ReflectionTestUtils.setField(product, "id", 1L, Long.class);

        // when
        ProductListResponseDto result = ProductListResponseDto.of(product, quantity);

        // then
        assertThat(result).extracting("productId", "productNo", "name", "price", "quantity")
                .contains(1L, "123", "빵빵이", 1000L, 2L);

    }

}