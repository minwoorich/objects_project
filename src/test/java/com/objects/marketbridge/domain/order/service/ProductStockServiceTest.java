package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.ProductStockService;
import com.objects.marketbridge.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.common.exception.error.CustomLogicException;
import com.objects.marketbridge.common.exception.error.ErrorCode;
import com.objects.marketbridge.common.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductStockServiceTest {

    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired ProductRepository productRepository;
    @Autowired
    ProductStockService productStockService;
    @BeforeEach
    void init() {

        Long quantity = 1L;

        List<Product> products = createProducts();
        productRepository.saveAll(products);

        List<OrderDetail> orderDetails = createOrderDetails(products, quantity);
        orderDetailRepository.saveAll(orderDetails);
    }
    @Test
    @DisplayName("주문이 들어오면 재고가 주문수량만큼 빠져나가야한다")
    void decrease() {

        //given
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Long> stocks = products.stream().map(Product::getStock).toList();
        List<Long> quantityList = orderDetails.stream().map(OrderDetail::getQuantity).toList();

        //when
        productStockService.decrease(orderDetails);

        //then
        IntStream.range(0, products.size())
                .forEach(i -> assertThat(products.get(i).getStock()).isEqualTo(stocks.get(i)-quantityList.get(i)));
    }

    private List<Product> createProducts() {

        Product product1 = Product.builder().stock(1L).build();
        Product product2 = Product.builder().stock(2L).build();
        Product product3 = Product.builder().stock(3L).build();

        return List.of(product1, product2, product3);
    }

    private List<OrderDetail> createOrderDetails(List<Product> products, Long quantity) {

        return products.stream().map(p -> OrderDetail.builder().product(p).quantity(quantity).build()).toList();
    }

    @Test
    @DisplayName("재고가 주문수량보다 적을 경우 예외를 던진다")
    void decrease_error() {

        //given
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<Product> products = productRepository.findAll();
        products.get(0).changeStock(0L); // 재고 0 으로 변경
        List<Long> stocks = products.stream().map(Product::getStock).toList();
        List<Long> quantityList = orderDetails.stream().map(OrderDetail::getQuantity).toList();

        //when, then
        assertThatThrownBy(() ->
                productStockService.decrease(orderDetails))
                .isInstanceOf(CustomLogicException.class)
                .hasMessageContaining(ErrorCode.OUT_OF_STOCK.getMessage());


    }
}