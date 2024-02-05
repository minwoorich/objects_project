package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.service.dto.OrderDto;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Slf4j
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
        Page<GetCancelReturnListDtio.Response> orderCancelReturnListResponsePage = orderDtoRepository.findOrdersByMemberId(member.getId(), PageRequest.of(0, 3));
        List<GetCancelReturnListDtio.Response> content = orderCancelReturnListResponsePage.getContent();
        // then
        assertThat(content).hasSize(2)
                .extracting("orderNo")
                .contains("123", "456");

        List<GetCancelReturnListDtio.OrderDetailInfo> detailResponses1Dao = content.get(0).getOrderDetailInfos();
        List<GetCancelReturnListDtio.OrderDetailInfo> detailResponses2Dao = content.get(1).getOrderDetailInfos();

        assertThat(detailResponses1Dao).hasSize(2)
                .extracting("orderNo", "productNo", "name", "price", "quantity", "orderStatus")
                .contains(
                        tuple("123", "1", "옷", 1000L, 1L, RETURN_COMPLETED.getCode()),
                        tuple("123", "2", "신발", 2000L, 2L, ORDER_CANCEL.getCode())
                );

        assertThat(detailResponses2Dao).hasSize(1)
                .extracting("orderNo", "productNo", "name", "price", "quantity", "orderStatus")
                .contains(
                        tuple("456", "3", "바지", 3000L, 3L, ORDER_CANCEL.getCode())
                );

    }

    @DisplayName("전체 주문 목록을 조회 할 경우 현재 사용자의 전체 주문 정보를 알 수 있다.")
    @Test
    void findByMemberIdWithMemberAddress(){

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
                = createCondition(1L, null, "1998");
        GetOrderHttp.Condition condition3
                = createCondition(1L, null, "2024");

        //when
        Page<OrderDto> orders = orderDtoRepository.findByMemberIdWithMemberAddress(condition1, page);
        orderDtoRepository.findByMemberIdWithMemberAddress(condition2, page);
        orderDtoRepository.findByMemberIdWithMemberAddress(condition3, page);

        //then
        assertThat(orders).hasSize(0);

    }

    @DisplayName("전체 주문 목록을 조회 할 경우 페이징이 가능하다")
    @Test
    void findByMemberIdWithMemberAddress_paging(){

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

        PageRequest pageSize1 = PageRequest.of(0, 1);
        PageRequest pageSize2 = PageRequest.of(0, 2);
        PageRequest pageSize2_1 = PageRequest.of(1, 2);

        GetOrderHttp.Condition condition
                = createCondition(1L, null, "2024");

        //when
//        orderDtoRepository.findByMemberIdWithMemberAddress(condition, pageSize1);
//        orderDtoRepository.findByMemberIdWithMemberAddress(condition, pageSize2);
        orderDtoRepository.findByMemberIdWithMemberAddress(condition, pageSize2_1);

        //then

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