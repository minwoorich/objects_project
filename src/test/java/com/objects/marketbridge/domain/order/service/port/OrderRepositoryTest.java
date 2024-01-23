package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.model.Member;
import com.objects.marketbridge.model.Product;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    MemberRepository memberRepository;

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

    @Test
    @DisplayName("유저가 반품, 취소한 상품들을 조회할 수 있다. ")
    public void findDistinctWithDetailsByMemberId() {
        // given
        Member member = Member.builder().build();

        // TODO 취소 접수일, 주문일 테스트 어떻게?
        Order order = Order.builder()
                .member(member)
                .orderNo("123")
                .build();

        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        Product product3 = Product.builder().build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .product(product1)
                .statusCode(StatusCodeType.RETURN_COMPLETED.getCode())
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .product(product2)
                .statusCode(StatusCodeType.ORDER_CANCEL.getCode())
                .build();
        OrderDetail orderDetail3 = OrderDetail.builder()
                .order(order)
                .product(product3)
                .statusCode(StatusCodeType.DELIVERY_DELAY.getCode())
                .build();

        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        productRepository.saveAll(List.of(product1, product2, product3));
        orderRepository.save(order);
        memberRepository.save(member);

        // when
        List<Order> orders = orderRepository.findDistinctWithDetailsByMemberId(member.getId());

        // then
        assertThat(orders).hasSize(1)
                .extracting("orderNo")
                .contains("123");

        List<OrderDetail> orderDetails = orders.get(0).getOrderDetails();
//        assertThat(orderDetails).hasSize(2);

    }
}