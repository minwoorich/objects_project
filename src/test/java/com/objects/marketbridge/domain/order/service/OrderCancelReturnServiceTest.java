//package com.objects.marketbridge.domain.order.service;
//
//import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
//import com.objects.marketbridge.domain.order.controller.response.ProductInfoResponse;
//import com.objects.marketbridge.domain.order.controller.response.CancelRefundInfoResponse;
//import com.objects.marketbridge.domain.order.dto.OrderReturnResponse;
//import com.objects.marketbridge.domain.order.dto.ReturnRefundInfoResponse;
//import com.objects.marketbridge.domain.order.entity.Order;
//import com.objects.marketbridge.domain.order.entity.OrderDetail;
//import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
//import com.objects.marketbridge.domain.order.service.port.OrderRepository;
//import com.objects.marketbridge.domain.product.repository.ProductRepository;
//import com.objects.marketbridge.model.Product;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class OrderCancelReturnServiceTest {
//
//    @Autowired
//    private OrderCancelReturnService orderCancelReturnService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderDetailRepository orderDetailRepository;
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    EntityManager em;
//
//    @Test
//    @DisplayName("주문 취소한 상품들, 환불 정보를 조회할 수 있다.")
//    public void requestCancel() {
//        // given
//        Product product1 = Product.builder()
//                .price(1000L)
//                .thumbImg("썸네일1")
//                .name("옷")
//                .build();
//        Product product2 = Product.builder()
//                .name("바지")
//                .price(2000L)
//                .thumbImg("썸네일2")
//                .build();
//        Product product3 = Product.builder()
//                .name("신발")
//                .price(3000L)
//                .thumbImg("썸네일3")
//                .build();
//
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .product(product1)
//                .quantity(2L)
//                .price(product1.getPrice() * 2L)
//                .build();
//        OrderDetail orderDetail2 = OrderDetail.builder()
//                .product(product2)
//                .quantity(3L)
//                .price(product2.getPrice() * 3L)
//                .build();
//        OrderDetail orderDetail3 = OrderDetail.builder()
//                .product(product3)
//                .quantity(4L)
//                .price(product3.getPrice() * 4L)
//                .build();
//
//        Order order = Order.builder().build();
//        order.addOrderDetail(orderDetail1);
//        order.addOrderDetail(orderDetail2);
//        order.addOrderDetail(orderDetail3);
//
//        List<Product> products = List.of(product1, product2, product3);
//        productRepository.saveAll(products);
//        orderRepository.save(order);
//
//        // when
//        OrderCancelResponse orderCancelResponse = orderCancelReturnService.requestCancel(order.getId(), List.of(product1.getId(), product2.getId(), product3.getId()));
//        List<ProductInfoResponse> productResponses = orderCancelResponse.getProductResponses();
//        CancelRefundInfoResponse cancelRefundInfoResponse = orderCancelResponse.getCancelRefundInfoResponse();
//
//        // then
//        assertThat(cancelRefundInfoResponse).extracting("deliveryFee", "refundFee", "discountPrice", "totalPrice")
//                .contains(0L, 0L, null, 20000L);
//
//        assertThat(productResponses).hasSize(3)
//                .extracting("quantity", "name", "price", "image")
//                .contains(
//                        tuple(2L, "옷", 1000L, "썸네일1"),
//                        tuple(3L, "바지", 2000L, "썸네일2"),
//                        tuple(4L, "신발", 3000L, "썸네일3")
//                );
//    }
//
//    @Test
//    @DisplayName("반품 요청한 상품들, 환불 정보를 조회할 수 있다.")
//    public void requestReturn() {
//        // given
//        Product product1 = Product.builder()
//                .price(1000L)
//                .thumbImg("썸네일1")
//                .name("옷")
//                .build();
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .product(product1)
//                .quantity(2L)
//                .price(product1.getPrice() * 2L)
//                .build();
//        Order order = Order.builder().build();
//        order.addOrderDetail(orderDetail1);
//
//        List<Product> products = List.of(product1);
//        productRepository.saveAll(products);
//        orderRepository.save(order);
//
//        // when
//        OrderReturnResponse orderReturnResponse = orderCancelReturnService.requestReturn(order.getId(), List.of(product1.getId()));
//        List<ProductInfoResponse> productResponses = orderReturnResponse.getProductResponses();
//        ReturnRefundInfoResponse cancelRefundInfoResponse = orderReturnResponse.getReturnRefundInfoResponse();
//
//        // then
//        assertThat(cancelRefundInfoResponse).extracting("deliveryFee", "returnFee", "productPrice")
//                .contains(0L, 0L, 2000L);
//
//        assertThat(productResponses).hasSize(1)
//                .extracting("quantity", "name", "price", "image")
//                .contains(
//                        tuple(2L, "옷", 1000L, "썸네일1")
//                );
//    }
//
//}