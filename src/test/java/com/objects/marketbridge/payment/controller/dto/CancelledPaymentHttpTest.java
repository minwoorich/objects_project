package com.objects.marketbridge.payment.controller.dto;

import com.objects.marketbridge.common.dto.KakaoPayOrderResponse;
import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.GetOrderService;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.PAYMENT_COMPLETED;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class CancelledPaymentHttpTest {

    @Autowired OrderCommendRepository orderCommendRepository;
    @Autowired OrderQueryRepository orderQueryRepository;
    @Autowired OrderDtoRepository orderDtoRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ProductRepository productRepository;

    @BeforeEach
    void init() {
        Address address = createAddress(createAddressValue("01011112222", "홍길동", "서울", "세종대로","11111", "민들레아파트110동3222호", "우리집"),true);
        Member member = Member.create(MembershipType.BASIC.getText(), "email.com", "1234", "홍길동", "01011112222", true, true);
        member.addAddress(address);
        memberRepository.save(member);

        Product product1 = Product.create(null, true, "상품1", 1000L, false, 100L, "썸네일1", 0L, "1번");
        Product product2 = Product.create(null, true, "상품2", 2000L, false, 100L, "썸네일2", 0L, "2번");
        Product product3 = Product.create(null, true, "상품3", 3000L, false, 100L, "썸네일3", 0L, "3번");
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderDetail orderDetail1 = OrderDetail.create("tid1", null, product1, "orderNo1", null, 1000L, 1L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail2 = OrderDetail.create("tid1", null, product2, "orderNo1", null, 2000L, 1L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail3 = OrderDetail.create("tid1", null, product3, "orderNo1", null, 3000L, 1L, null, PAYMENT_COMPLETED.getCode());

        Order order = createOrder(member, address, "상품1 외 2건", "orderNo1", 6000L, "tid1", List.of(orderDetail1, orderDetail2, orderDetail3), null);
        orderCommendRepository.save(order);
    }

    private Address createAddress(AddressValue addressValue, Boolean isDefault) {
        return Address.create(addressValue, isDefault);
    }

    private AddressValue createAddressValue(String phoneNo, String name, String city, String street, String zipcode, String detail, String alias) {
        return AddressValue.create(phoneNo, name, city, street, zipcode, detail, alias);
    }

    private Order createOrder(Member member, Address address, String orderName, String orderNo, Long totalPrice, String tid, List<OrderDetail> orderDetails, Payment payment) {
        Order order = Order.create(member, address, orderName, orderNo, totalPrice, tid);

        // order <-> orderDetail 연관관계
        orderDetails.forEach(order::addOrderDetail);

        return order;
    }


    @DisplayName("KakaoPayOrderResponse와 Order엔티티 를 통해 CancelledPaymentHttp.Response 가 생성된다")
    @Test
    void of_card() {
        // given
        Order order = orderQueryRepository.findByOrderNo("orderNo1");
        KakaoPayOrderResponse kakaoResponse = KakaoPayOrderResponse.builder()
                .tid("tid")
                .cid("cid")
                .status("실패")
                .partnerOrderId("orderNo1")
                .partnerUserId("1")
                .paymentMethodType("카드")
                .amount(Amount.create(6000L, 0L, 0L))
                .itemName("상품1 외 2건")
                .selectedCardInfo(CardInfo.create("카카오뱅크", "카카오뱅크", "0"))
                .canceledAt(LocalDateTime.of(2024, 1, 1, 12, 0, 0))
                .approvedAt(LocalDateTime.of(2024,1,1,11,0,0))
                .build();

        // when
        CancelledPaymentHttp.Response result = CancelledPaymentHttp.Response.of(kakaoResponse, order);

        //then
        Assertions.assertThat(result.getPaymentMethodType()).isEqualTo("카드");
        Assertions.assertThat(result.getOrderName()).isEqualTo("상품1 외 2건");
        Assertions.assertThat(result.getApprovedAt()).isEqualTo("2024-01-01 11:00:00");
        Assertions.assertThat(result.getCanceledAt()).isEqualTo("2024-01-01 12:00:00");
        Assertions.assertThat(result.getStatus()).isEqualTo("실패");
        Assertions.assertThat(result.getCardIssuerName()).isEqualTo("카카오뱅크");
        Assertions.assertThat(result.getCardPurchaseName()).isEqualTo("카카오뱅크");
        Assertions.assertThat(result.getTotalAmount()).isEqualTo(6000L);

        Assertions.assertThat(result.getProductInfos()).hasSize(3);
        Assertions.assertThat(result.getProductInfos().get(0).getThumbImgUrl()).isEqualTo("썸네일1");
        Assertions.assertThat(result.getProductInfos().get(0).getPrice()).isEqualTo(1000L);
    }

    @DisplayName("")
    @Test
    void of_CASH(){
        //given

        //when

        //then
    }



}