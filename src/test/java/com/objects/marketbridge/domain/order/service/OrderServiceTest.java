package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @BeforeEach
    public void createProduct() {

    }

    @DisplayName("주문에 대한 정보를 전달받은뒤 주문을 생성한다.")
    @Test
    void createOrder(){
        //given

        //when

        //then
    }

}