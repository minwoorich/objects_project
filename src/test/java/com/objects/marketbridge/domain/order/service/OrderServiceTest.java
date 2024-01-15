package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.product.repository.ProductJpaRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired ProductRepository productRepository;
    @Autowired ProductJpaRepository productJpaRepository;

    @AfterEach
    void tearDown() {
        productJpaRepository.deleteAllInBatch();
    }

    @DisplayName("주문에 대한 정보를 전달받은뒤 주문을 생성한다.")
    @Test
    void simpleCreateOrder(){
        //given
        Product product1 = Product.builder().name("가방").price(1000).build();
        Product product2 = Product.builder().name("신발").price(2000).build();
        Product product3 = Product.builder().name("세제").price(3000).build();

        productJpaRepository.saveAll(List.of(product1, product2, product3));



        //when


        //then
    }

}