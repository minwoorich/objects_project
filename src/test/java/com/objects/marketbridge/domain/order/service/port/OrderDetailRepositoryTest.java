package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.model.*;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderDetailRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired EntityManager em;


    @Test
    @DisplayName("특정 주문에 해당하는 주문 상세의 상태 코드를 한번에 바꾼다.")
    public void changeAllType() {
        // given
        String givenCodeType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        String changeCodeType = StatusCodeType.ORDER_CANCEL.getCode();

        OrderDetail orderDetail1 = createOrderDetail_type(givenCodeType);
        OrderDetail orderDetail2 = createOrderDetail_type(givenCodeType);
        OrderDetail orderDetail3 = createOrderDetail_type(givenCodeType);

        Order order = Order.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // when
        int result = orderDetailRepository.changeAllType(orderId, changeCodeType);

        // then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 상세 리스트를 모두 저장한다.")
    public void saveAll() {
        // given
        String givenType = StatusCodeType.PAYMENT_COMPLETED.getCode();
        OrderDetail orderDetail1 = createOrderDetail_type(givenType);
        OrderDetail orderDetail2 = createOrderDetail_type(givenType);
        OrderDetail orderDetail3 = createOrderDetail_type(givenType);
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2, orderDetail3);

        // when
        List<OrderDetail> savedOrderDetails = orderDetailRepository.saveAll(orderDetails);

        // then
        assertThat(savedOrderDetails.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 상세에 이유 넣기")
    public void addReason() {
        // given
        String reason = "상품이 맘에들지 않아요";
        OrderDetail orderDetail1 = OrderDetail.builder().build();
        OrderDetail orderDetail2 = OrderDetail.builder().build();
        OrderDetail orderDetail3 = OrderDetail.builder().build();

        Order order = Order.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getId();

        // when
        orderDetailRepository.addReason(orderId, reason);

        // then
        String savedReason = getReason(orderId);
        assertThat(reason).isEqualTo(savedReason);
    }

    @Test
    @DisplayName("주문 ID와 상품 리스트가 주어지면 주문 상세 리스트를 조회할 수 있다.")
    public void findByProdOrder_IdAndProductIn() {
        // given
        Product product1 = Product.builder().name("옷").build();
        Product product2 = Product.builder().name("바지").build();
        Product product3 = Product.builder().name("신발").build();
        
        OrderDetail orderDetail1 = OrderDetail.builder().product(product1).build();
        OrderDetail orderDetail2 = OrderDetail.builder().product(product2).build();
        OrderDetail orderDetail3 = OrderDetail.builder().product(product3).build();

        Order order = Order.builder().build();
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        order.addOrderDetail(orderDetail3);

        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);
        orderRepository.save(order);

        // when
        List<OrderDetail> orderDetails = orderDetailRepository.findByProdOrder_IdAndProductIn(order.getId(), products);

        // then
        Assertions.assertThat(orderDetails).hasSize(3)
                .extracting("product", "order")
                .contains(
                        tuple(product1, order),
                        tuple(product2, order),
                        tuple(product3, order)
                );
    }

    private String getReason(Long orderId) {
        List<OrderDetail> orderDetails = orderRepository.findById(orderId).get().getOrderDetails();
        return orderDetails.get(0).getReason();
    }

    private OrderDetail createOrderDetail_type(String code) {
        return OrderDetail.builder()
                .statusCode(code)
                .build();
    }


}