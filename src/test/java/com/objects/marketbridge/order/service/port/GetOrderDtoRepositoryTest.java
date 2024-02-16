package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.GetOrderDto;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.coupon.CouponRepository;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Slf4j
class GetOrderDtoRepositoryTest {

    @Autowired
    OrderCommendRepository orderCommendRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderDtoRepository orderDtoRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    EntityManager entityManager;

    @DisplayName("상세 주문 조회 하기(member, address, orderDetails, product, payment 전부 fetch join)")
    @Test
    void getOrderDetails() {
        // given
        Member member = createMember("1");
        Address address = createAddress("서울", "세종대로", "민들레아파트");
        member.addAddress(address);
        memberRepository.save(member);

        Product product1 = createProduct(1000L, "1");
        Product product2 = createProduct(2000L, "2");
        Product product3 = createProduct(3000L, "3");
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderDetail orderDetail1 = createOrderDetail(product1,  1L, "1");
        OrderDetail orderDetail2 = createOrderDetail(product2,  1L, "1");
        OrderDetail orderDetail3 = createOrderDetail(product3,  1L, "1");

        Payment payment = createPayment("카드", "카카오뱅크");

        Order order = createOrder(member, address, "1", List.of(orderDetail1, orderDetail2, orderDetail3), payment);

        orderCommendRepository.save(order);

        // when
        GetOrderDto getOrderDto = orderDtoRepository.findByOrderNo(order.getOrderNo());

        //then
        assertThat(getOrderDto.getMemberId()).isEqualTo(member.getId());
        assertThat(getOrderDto.getAddress()).isEqualTo(address.getAddressValue());

        assertThat(getOrderDto.getOrderDetails()).hasSize(3);
        assertThat(getOrderDto.getOrderDetails().get(0).getOrderDetailId()).isEqualTo(orderDetail1.getId());
        assertThat(getOrderDto.getOrderDetails().get(1).getOrderDetailId()).isEqualTo(orderDetail2.getId());
        assertThat(getOrderDto.getOrderDetails().get(2).getOrderDetailId()).isEqualTo(orderDetail3.getId());

        assertThat(getOrderDto.getOrderDetails().get(0).getProduct().getProductId()).isEqualTo(product1.getId());
        assertThat(getOrderDto.getOrderDetails().get(1).getProduct().getProductId()).isEqualTo(product2.getId());
        assertThat(getOrderDto.getOrderDetails().get(2).getProduct().getProductId()).isEqualTo(product3.getId());

        assertThat(getOrderDto.getPaymentMethod()).isEqualTo(payment.getPaymentMethod());
        assertThat(getOrderDto.getCardIssuerName()).isEqualTo(payment.getCardInfo().getCardIssuerName());

    }

    private Payment createPayment(String paymentMethod, String cardIssuerName) {
        CardInfo cardInfo = CardInfo.builder()
                .cardIssuerName(cardIssuerName)
                .build();

        return Payment.builder()
                .paymentMethod(paymentMethod)
                .cardInfo(cardInfo)
                .build();
    }

    private Order createOrder(Member member1, Address address, String orderNo, List<OrderDetail> orderDetails, Payment payment) {

        Order order = Order.builder()
                .member(member1)
                .address(address)
                .orderNo(orderNo)
                .build();

        // order <-> orderDetail 연관관계
        orderDetails.forEach(order::addOrderDetail);

        // payment <-> payment 연관관계
        if (payment != null) {
            order.linkPayment(payment);
        }

        return order;
    }

    private OrderDetail createOrderDetail(Product product,  Long quantity, String orderNo) {
        return OrderDetail.builder()
                .product(product)
                .quantity(quantity)
                .reducedQuantity(0L)
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

    private Address createAddress(String city, String street, String detail) {
        return Address.builder().addressValue(AddressValue.builder()
                        .city(city)
                        .street(street)
                        .detail(detail).build())
                .build();
    }

}