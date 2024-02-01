package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductInfoResponseDtoTest {

    @Test
    @DisplayName("주문 상세를 ProductInfoResponseDto로 변환")
    public void of() {
        // given
        Product product1 = Product.builder()
                .price(1000L)
                .name("빵빵이")
                .thumbImg("빵빵이 이미지")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .product(product1)
                .price(product1.getPrice())
                .quantity(2L)
                .build();

        // when
        ProductInfoResponseDto result = ProductInfoResponseDto.of(orderDetail);

        // then
        assertThat(result).extracting("quantity", "name", "price", "image")
                .contains(2L, "빵빵이", 1000L, "빵빵이 이미지");
    }

}