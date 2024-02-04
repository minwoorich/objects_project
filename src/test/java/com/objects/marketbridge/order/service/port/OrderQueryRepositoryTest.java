package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class OrderQueryRepositoryTest {

    @Autowired private OrderCommendRepository orderCommendRepository;
    @Autowired private OrderQueryRepository orderQueryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MemberRepository memberRepository;

    @DisplayName("전체 주문 목록을 조회 할 경우 현재 사용자의 전체 주문 정보를 알 수 있다.")
    @Test
    void findAllWithMemberOrderDetailProduct(){

        //given
        Member member = createMember("1");
        memberRepository.save(member);

        Product product1 = createProduct(1000L, "1");
        Product product2 = createProduct(2000L, "2");
        Product product3 = createProduct(3000L, "3");
        Product product4 = createProduct(4000L, "4");
        productRepository.saveAll(List.of(product1, product2, product3, product4));

        OrderDetail orderDetail1 = createOrderDetail(product1, 1L, "1");
        OrderDetail orderDetail2 = createOrderDetail(product2, 1L, "1");
        OrderDetail orderDetail3 = createOrderDetail(product3, 1L, "1");

        OrderDetail orderDetail4 = createOrderDetail(product1, 2L, "2");
        OrderDetail orderDetail5 = createOrderDetail(product2, 2L, "2");
        OrderDetail orderDetail6 = createOrderDetail(product4, 2L, "2");

        OrderDetail orderDetail7 = createOrderDetail(product1, 3L, "3");
        OrderDetail orderDetail8 = createOrderDetail(product3, 3L, "3");
        OrderDetail orderDetail9 = createOrderDetail(product4, 3L, "3");

        OrderDetail orderDetail10 = createOrderDetail(product2, 4L, "4");
        OrderDetail orderDetail11 = createOrderDetail(product3, 4L, "4");
        OrderDetail orderDetail12 = createOrderDetail(product4, 4L, "4");

        Order order1 = createOrder(member, "1", List.of(orderDetail1, orderDetail2, orderDetail3));
        Order order2 = createOrder(member, "2", List.of(orderDetail4, orderDetail5, orderDetail6));
        Order order3 = createOrder(member, "3", List.of(orderDetail7, orderDetail8, orderDetail9));
        Order order4 = createOrder(member, "4", List.of(orderDetail10, orderDetail11, orderDetail12));
        orderCommendRepository.saveAll(List.of(order1, order2, order3, order4));

        PageRequest page = PageRequest.of(0, 10);

        GetOrderHttp.Condition condition1
                = createCondition(1L, null, null);
        GetOrderHttp.Condition condition2
                = createCondition(1L, "상품", null);
        GetOrderHttp.Condition condition3
                = createCondition(1L, "상품1", null);
        GetOrderHttp.Condition condition4
                = createCondition(1L, null, "2024");
        GetOrderHttp.Condition condition5
                = createCondition(1L, "상품3", "2024");
        GetOrderHttp.Condition condition6
                = createCondition(1L, "품3", "2024");
        GetOrderHttp.Condition condition7
                = createCondition(1L, "품3", "1998");
        GetOrderHttp.Condition condition8
                = createCondition(1L, "없는상품", "2024");

        //when
        List<Order> orders_cond1 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition1);
        List<Order> orders_cond2 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition2);
        List<Order> orders_cond3 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition3);
        List<Order> orders_cond4 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition4);
        List<Order> orders_cond5 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition5);
        List<Order> orders_cond6 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition6);
        List<Order> orders_cond7 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition7);
        List<Order> orders_cond8 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page, condition8);

        //then
        assertThat(orders_cond1).hasSize(4);
        assertThat(orders_cond2).hasSize(4);
        assertThat(orders_cond3).hasSize(3);
        assertThat(orders_cond4).hasSize(4);
        assertThat(orders_cond5).hasSize(3);
        assertThat(orders_cond6).hasSize(3);
        assertThat(orders_cond7).hasSize(0);
        assertThat(orders_cond8).hasSize(0);

    }

    @DisplayName("전체 주문 목록을 조회 할 경우 페이징이 가능하다")
    @Test
    void findAllWithMemberOrderDetailProduct_paging(){

        //given
        Member member = createMember("1");
        memberRepository.save(member);

        Product product1 = createProduct(1000L, "1");
        Product product2 = createProduct(2000L, "2");
        Product product3 = createProduct(3000L, "3");
        Product product4 = createProduct(4000L, "4");
        productRepository.saveAll(List.of(product1, product2, product3, product4));

        OrderDetail orderDetail1 = createOrderDetail(product1, 1L, "1");
        OrderDetail orderDetail2 = createOrderDetail(product2, 1L, "1");
        OrderDetail orderDetail3 = createOrderDetail(product3, 1L, "1");

        OrderDetail orderDetail4 = createOrderDetail(product1, 2L, "2");
        OrderDetail orderDetail5 = createOrderDetail(product2, 2L, "2");
        OrderDetail orderDetail6 = createOrderDetail(product4, 2L, "2");

        OrderDetail orderDetail7 = createOrderDetail(product1, 3L, "3");
        OrderDetail orderDetail8 = createOrderDetail(product3, 3L, "3");
        OrderDetail orderDetail9 = createOrderDetail(product4, 3L, "3");

        OrderDetail orderDetail10 = createOrderDetail(product2, 4L, "4");
        OrderDetail orderDetail11 = createOrderDetail(product3, 4L, "4");
        OrderDetail orderDetail12 = createOrderDetail(product4, 4L, "4");

        Order order1 = createOrder(member, "1", List.of(orderDetail1, orderDetail2, orderDetail3));
        Order order2 = createOrder(member, "2", List.of(orderDetail4, orderDetail5, orderDetail6));
        Order order3 = createOrder(member, "3", List.of(orderDetail7, orderDetail8, orderDetail9));
        Order order4 = createOrder(member, "4", List.of(orderDetail10, orderDetail11, orderDetail12));
        orderCommendRepository.saveAll(List.of(order1, order2, order3, order4));

        PageRequest page1 = PageRequest.of(0, 1);
        PageRequest page2 = PageRequest.of(1, 1);

        GetOrderHttp.Condition condition
                = createCondition(1L, "상품", "2024");

        //when
        List<Order> orders_page1 = orderQueryRepository.findAllWithMemberOrderDetailProduct(page1, condition);

        //then
        assertThat(orders_page1).hasSize(1);

    }

    private GetOrderHttp.Condition createCondition(Long memberId, String keyword, String year) {
        return GetOrderHttp.Condition.builder()
                .memberId(memberId)
                .keyword(keyword)
                .year(year)
                .build();
    }

    private Order createOrder(Member member1, String orderNo, List<OrderDetail> orderDetails) {

        Order order = Order.builder()
                .member(member1)
                .orderNo(orderNo)
                .build();

        // order <-> orderDetail 연관관계
        orderDetails.forEach(order::addOrderDetail);

        return order;
    }

    private OrderDetail createOrderDetail(Product product, Long quantity, String orderNo) {
        return OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .price(product.getPrice() * quantity)
                .orderNo(orderNo)
                .build();
    }

    private Product createProduct(Long price, String no) {
        return Product.builder()
                .price(price)
                .thumbImg("썸네일"+no)
                .name("상품"+no)
                .build();
    }

    private Member createMember(String no) {
        return Member.builder()
                .name("홍길동"+no)
                .build();
    }
}