package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.model.Product;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("주문ID로 주문을 조회한다.")
    public void findByOrderId() {
        // given
        Order order = Order.builder().build();
        Order savedOrder = orderRepository.save(order);

        // when
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        // then
        assertTrue(foundOrder.isPresent());
        assertThat(savedOrder.getId()).isEqualTo(foundOrder.get().getId());
    }

    @Test
    @DisplayName("저장된 주문이 없다면 주문을 조회 할 수 없다.")
    public void findByOrderIdNoSearch() {
        // given
        Long NoId = 1L;

        // when
        Optional<Order> empty = orderRepository.findById(NoId);

        // then
        assertFalse(empty.isPresent());
        assertThat(empty).isEmpty();
    }

    @Test
    @DisplayName("주문을 저장한다.")
    public void save() {
        // given
        Order order = Order.builder().build();

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        assertNotNull(savedOrder.getId());

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());
        assertTrue(foundOrder.isPresent());
        assertThat(savedOrder.getId()).isEqualTo(foundOrder.get().getId());
    }

    @Test
    @DisplayName("주문 아이디로 주문, 주문상세, 상품을 한번에 조회 할 수 있다.")
    public void findOrderWithDetailsAndProduct() {
        // given
        Order order = Order.builder().build();
        Product product = Product.builder().build();
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .build();
        order.addOrderDetail(orderDetail);
        productRepository.save(product);
        orderRepository.save(order);

        // when
        Order findOrder = orderRepository.findOrderWithDetailsAndProduct(order.getId()).get();

        // then
        assertThat(findOrder.getId()).isEqualTo(order.getId());
    }


    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Test
    @DisplayName("")
    public void test() {
        // given
        Order order = Order.builder().build();
        Product product = Product.builder().build();
        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .price(1000L)
                .product(product)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .price(2000L)
                .product(product)
                .build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);

        // when
        orderRepository.save(order);

        // then
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetail1.getId());
        System.out.println("orderDetail = " + orderDetail);

    }

}