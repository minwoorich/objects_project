package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.infra.dao.CancelReturnResponseDao;
import com.objects.marketbridge.order.infra.dao.DetailResponseDao;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderDtoRepositoryTest {

    @Autowired
    OrderCommendRepository orderCommendRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderDtoRepository orderDtoRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    @DisplayName("유저가 반품, 취소한 상품들을 조회할 수 있다.")
    public void findOrdersByMemberId() {
        // given
        Member member = Member.builder().build();

        Order order1 = Order.builder()
                .member(member)
                .orderNo("123")
                .build();

        Order order2 = Order.builder()
                .member(member)
                .orderNo("456")
                .build();

        Product product1 = Product.builder()
                .productNo("1")
                .name("옷")
                .price(1000L)
                .build();
        Product product2 = Product.builder()
                .productNo("2")
                .name("신발")
                .price(2000L)
                .build();
        Product product3 = Product.builder()
                .productNo("3")
                .name("바지")
                .price(3000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order1)
                .product(product1)
                .quantity(1L)
                .orderNo(order1.getOrderNo())
                .statusCode(RETURN_COMPLETED.getCode())
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order1)
                .product(product2)
                .quantity(2L)
                .orderNo(order1.getOrderNo())
                .statusCode(ORDER_CANCEL.getCode())
                .build();
        OrderDetail orderDetail3 = OrderDetail.builder()
                .order(order2)
                .product(product3)
                .quantity(3L)
                .orderNo(order2.getOrderNo())
                .statusCode(ORDER_CANCEL.getCode())
                .build();
        OrderDetail orderDetail4 = OrderDetail.builder()
                .order(order2)
                .product(product2)
                .quantity(4L)
                .orderNo(order2.getOrderNo())
                .statusCode(DELIVERY_ING.getCode())
                .build();

        order1.addOrderDetail(orderDetail1);
        order1.addOrderDetail(orderDetail2);
        order2.addOrderDetail(orderDetail3);
        order2.addOrderDetail(orderDetail4);

        productRepository.saveAll(List.of(product1, product2, product3));
        memberRepository.save(member);
        orderCommendRepository.save(order1);
        orderCommendRepository.save(order2);

        // when
        Page<CancelReturnResponseDao> orderCancelReturnListResponsePage = orderDtoRepository.findOrdersByMemberId(member.getId(), PageRequest.of(0, 3));
        List<CancelReturnResponseDao> content = orderCancelReturnListResponsePage.getContent();
        // then
        assertThat(content).hasSize(2)
                .extracting("orderNo")
                .contains("123", "456");

        List<DetailResponseDao> detailResponses1Dao = content.get(0).getDetailResponseDaos();
        List<DetailResponseDao> detailResponses2Dao = content.get(1).getDetailResponseDaos();

        assertThat(detailResponses1Dao).hasSize(2)
                .extracting("orderNo", "productId", "productNo", "name", "price", "quantity", "orderStatus")
                .contains(
                        tuple("123", 1L, "1", "옷", 1000L, 1L, RETURN_COMPLETED.getCode()),
                        tuple("123", 2L, "2", "신발", 2000L, 2L, ORDER_CANCEL.getCode())
                );

        assertThat(detailResponses2Dao).hasSize(1)
                .extracting("orderNo", "productId", "productNo", "name", "price", "quantity", "orderStatus")
                .contains(
                        tuple("456", 3L, "3", "바지", 3000L, 3L, ORDER_CANCEL.getCode())
                );

    }

}