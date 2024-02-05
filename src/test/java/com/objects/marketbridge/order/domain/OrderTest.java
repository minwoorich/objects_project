package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OrderTest {

    @Autowired
    private OrderCommendRepository orderCommendRepository;
    @Autowired
    private OrderQueryRepository orderQueryRepository;
    @Autowired
    private OrderDetailCommendRepository orderDetailCommendRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("주문 취소시 사용한 유저 쿠폰이 모두 반환되야 한다.")
    public void returnCoupon() {
        // given
        LocalDateTime useDate = LocalDateTime.of(2024, 1, 16, 7, 14);

        Order order = Order.builder()
                .build();

        Product product1 = Product.builder()
                .build();
        Product product2 = Product.builder()
                .build();

        Coupon coupon1 = Coupon.builder()
                .product(product1)
                .price(1000L)
                .count(10L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .product(product2)
                .price(2000L)
                .count(20L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .coupon(coupon1)
                .product(product1)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .coupon(coupon2)
                .product(product2)
                .build();

        MemberCoupon memberCoupon1 = MemberCoupon.builder()
                .coupon(coupon1)
                .isUsed(true)
                .usedDate(useDate)
                .build();
        MemberCoupon memberCoupon2 = MemberCoupon.builder()
                .coupon(coupon2)
                .isUsed(true)
                .usedDate(useDate)
                .build();

        orderCommendRepository.save(order);
        orderDetailCommendRepository.saveAll(List.of(orderDetail1, orderDetail2));
        productRepository.saveAll(List.of(product1, product2));
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        coupon1.addMemberCoupon(memberCoupon1);
        coupon2.addMemberCoupon(memberCoupon2);
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        memberCouponRepository.save(memberCoupon1);
        memberCouponRepository.save(memberCoupon2);

        Order findOrder = orderQueryRepository.findById(order.getId()).get();

        // when
        findOrder.changeMemberCouponInfo(
                TestDateTimeHolder.builder()
                        .now(null)
                        .build()
        );

        // then
        assertThat(coupon1.getCount()).isEqualTo(10L);
        assertThat(coupon2.getCount()).isEqualTo(20L);
        assertThat(memberCoupon1.getUsedDate()).isNull();
        assertThat(memberCoupon2.getUsedDate()).isNull();
        assertThat(memberCoupon1.getIsUsed()).isFalse();
        assertThat(memberCoupon2.getIsUsed()).isFalse();
    }

    @DisplayName("주문생성시 사용한 쿠폰들의 사용여부와 사용날짜가 세팅되어야한다.")
    @Test
    void useCoupon(){
        LocalDateTime useDate = LocalDateTime.of(2024, 1, 16, 7, 14);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(useDate)
                .build();

        Order order = Order.builder()
                .build();

        Product product1 = Product.builder()
                .build();
        Product product2 = Product.builder()
                .build();

        Coupon coupon1 = Coupon.builder()
                .product(product1)
                .price(1000L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .product(product2)
                .price(2000L)
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .coupon(coupon1)
                .product(product1)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .coupon(coupon2)
                .product(product2)
                .build();

        MemberCoupon memberCoupon1 = MemberCoupon.builder()
                .coupon(coupon1)
                .isUsed(false)
                .usedDate(null)
                .build();
        MemberCoupon memberCoupon2 = MemberCoupon.builder()
                .coupon(coupon2)
                .isUsed(false)
                .usedDate(null)
                .build();

        Order savedOrder = orderCommendRepository.save(order);
        orderDetailCommendRepository.saveAll(List.of(orderDetail1, orderDetail2));
        productRepository.saveAll(List.of(product1, product2));
        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);
        em.persist(coupon1);
        em.persist(coupon2);
        em.persist(memberCoupon1);
        em.persist(memberCoupon2);
        coupon1.addMemberCoupon(memberCoupon1);
        coupon2.addMemberCoupon(memberCoupon2);


        // when
        savedOrder.changeMemberCouponInfo(dateTimeHolder);

        // then
        assertThat(memberCoupon1.getUsedDate()).isEqualTo(useDate);
        assertThat(memberCoupon2.getUsedDate()).isEqualTo(useDate);
        assertThat(memberCoupon1.getIsUsed()).isTrue();
        assertThat(memberCoupon2.getIsUsed()).isTrue();
    }

    @DisplayName("판매자 별로 상세주문(OrderDetail) 을 그룹핑할 수 있다")
    @Test
    void orderDetailsGroupedBySellerId(){

        //given
        Order order = createOrder();
        List<OrderDetail> orderDetails = createOrderDetails();
        orderDetails.forEach(order::addOrderDetail);
        orderCommendRepository.save(order);

        //when
        Map<Long, List<OrderDetail>> groupedMap =
                order.orderDetailsGroupedBySellerId();

        //then
        assertThat(getOrderDetails(order)).hasSize(4);
        assertThat(groupedMap.keySet()).hasSize(3);
        assertThat(groupedMap.keySet())
                .containsExactlyInAnyOrderElementsOf(getKeySet(orderDetails));
        assertThat(groupedMap.get(getKeyList(orderDetails).get(0)))
                .containsExactlyInAnyOrderElementsOf(List.of(orderDetails.get(0), orderDetails.get(1)));

    }

    @DisplayName("판매자 별로 상세주문(OrderDetail) 을 그룹핑할 수 있다")
    @Test
    void totalAmountGroupedBySellerId(){

        //given
        Order order = createOrder();
        List<OrderDetail> orderDetails = createOrderDetails();
        orderDetails.forEach(order::addOrderDetail);
        orderCommendRepository.save(order);

        //when

        Map<Long, Long> groupedMap = order.totalAmountGroupedBySellerId();

        //then
        assertThat(getOrderDetails(order)).hasSize(4);
        assertThat(groupedMap.keySet()).hasSize(3);
        assertThat(groupedMap.get(getKeyList(orderDetails).get(0))).isEqualTo(3000L);
    }

    private List<OrderDetail> createOrderDetails() {
        OrderDetail orderDetail1 = OrderDetail.builder()
                .price(1000L)
                .quantity(1L)
                .sellerId(1L)
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .price(1000L)
                .quantity(2L)
                .sellerId(1L)
                .build();
        OrderDetail orderDetail3 = OrderDetail.builder()
                .price(1000L)
                .quantity(2L)
                .sellerId(2L)
                .build();
        OrderDetail orderDetail4 = OrderDetail.builder()
                .price(1000L)
                .quantity(3L)
                .sellerId(3L)
                .build();
        return List.of(orderDetail1, orderDetail2, orderDetail3, orderDetail4);
    }

    private List<OrderDetail> getOrderDetails(Order order) {
        return order.getOrderDetails();
    }

    private Set<Long> getKeySet(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(OrderDetail::getSellerId).collect(Collectors.toSet());
    }
    private List<Long> getKeyList(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(OrderDetail::getSellerId).collect(Collectors.toList());
    }

    private Order createOrder() {
        return Order.builder()
                .orderNo("aaaa-aaaa-aaaa")
                .totalPrice(4000L)
                .build();
    }



}