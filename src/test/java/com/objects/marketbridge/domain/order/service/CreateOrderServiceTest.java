package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.coupon.repository.MemberCouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.error.ErrorCode;
import com.objects.marketbridge.model.*;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.product.repository.ProductJpaRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CreateOrderServiceTest {

    @Autowired CreateOrderService createOrderService;
    @Autowired ProductRepository productRepository;
    @Autowired ProductJpaRepository productJpaRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired AddressRepository addressRepository;
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired MemberCouponRepository memberCouponRepository;

    @BeforeEach
    void init(){
        // member 생성
        Member member = createMember();
        memberRepository.save(member);

        // address 생성
        Address address = createAddress(member);
        addressRepository.save(address);

        // product 생성
        List<Product> products = createProducts();
        productRepository.saveAll(products);

        // coupon 생성
        List<Coupon> coupons = createCoupons(products);
        couponRepository.saveAll(coupons);

        // memberCoupon 생성
        List<MemberCoupon> memberCoupons = createMemberCoupons(member, coupons);
        memberCouponRepository.saveAll(memberCoupons);

    }

    private  Member createMember() {

        return Member.builder()
                .email("hong@email.com")
                .name("홍길동").build();
    }

    private  Address createAddress(Member member) {

        return Address.builder()
                .member(member).build();
    }

    private List<Product> createProducts() {

        Product product1 = Product.builder()
                .name("가방")
                .price(1000L)
                .stock(5L).build();

        Product product2 = Product.builder()
                .name("티비")
                .stock(5L)
                .price(2000L).build();

        Product product3 = Product.builder()
                .name("워치")
                .stock(5L)
                .price(3000L).build();

        return List.of(product1, product2, product3);
    }

    private List<Coupon> createCoupons(List<Product> products) {

        Coupon coupon1 = Coupon.builder()
                .price(2000L)
                .product(products.get(0))
                .name("가방쿠폰").build();

        Coupon coupon2 = Coupon.builder()
                .price(2000L)
                .product(products.get(1))
                .name("티비쿠폰").build();

        return List.of(coupon1, coupon2);
    }

    private List<MemberCoupon> createMemberCoupons(Member member, List<Coupon> coupons) {

        MemberCoupon memberCoupon1 = MemberCoupon.builder()
                .member(member)
                .coupon(coupons.get(0))
                .isUsed(false).build();

        MemberCoupon memberCoupon2 = MemberCoupon.builder()
                .member(member)
                .coupon(coupons.get(1))
                .isUsed(false).build();

        return List.of(memberCoupon1, memberCoupon2);
    }

    @DisplayName("주문 생성시 Order 를 생성 한다.")
    @Test
    void CreateOrder(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);

        //when
        createOrderService.create(createOrderDto);
        Order order = orderRepository.findByOrderNo(createOrderDto.getOrderNo());

        //then
        assertThat(order.getMember().getId()).isEqualTo(member.getId());
        assertThat(order.getAddress()).isEqualTo(address);
        assertThat(order.getOrderName()).isEqualTo("가방 외 2건");
        assertThat(order.getOrderNo()).isEqualTo("aaaa-aaaa-aaaa");
        assertThat(order.getTotalPrice()).isEqualTo(createOrderDto.getTotalOrderPrice());
        assertThat(order.getRealPrice()).isEqualTo(createOrderDto.getRealOrderPrice());
    }

    private long getTotalCouponPrice(List<Coupon> coupons) {

        return coupons.stream().mapToLong(Coupon::getPrice).sum();
    }

    private long getTotalOrderPrice(List<ProductValue> productValues) {

        return productValues.stream().mapToLong(p ->
                productRepository.findById(p.getProductId()).getPrice() * p.getQuantity()
        ).sum();
    }

    @DisplayName("주문 생성시 OrderDetail 을 생성 한다.")
    @Test
    void CreateOrderDetail(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);

        //when
        createOrderService.create(createOrderDto);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderNo(createOrderDto.getOrderNo());

        //then
        assertThat(orderDetails).hasSize(3);

        for (int i = 0; i < orderDetails.size(); i++) {
            assertThat(orderDetails.get(i).getProduct()).isEqualTo(productRepository.findById(createOrderDto.getProductValues().get(i).getProductId()));
            assertThat(orderDetails.get(i).getOrderNo()).isEqualTo(createOrderDto.getOrderNo());
            assertThat(orderDetails.get(i).getQuantity()).isEqualTo(createOrderDto.getProductValues().get(i).getQuantity());
            assertThat(orderDetails.get(i).getPrice()).isEqualTo(productRepository.findById(createOrderDto.getProductValues().get(i).getProductId()).getPrice());
            assertThat(orderDetails.get(i).getStatusCode()).isEqualTo(StatusCodeType.ORDER_INIT.getCode());
        }
    }

    @DisplayName("쿠폰을 사용한 OrderDetail 에는 사용했던 쿠폰이 등록되어있어야한다.")
    @Test
    void OrderDetailWithCoupon(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);
        List<Coupon> coupons = couponRepository.findAll();

        //when
        createOrderService.create(createOrderDto);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderNo(createOrderDto.getOrderNo());

        //then
        assertThat(orderDetails.get(0).getCoupon().getName()).isEqualTo(coupons.get(0).getName());
        assertThat(orderDetails.get(1).getCoupon().getName()).isEqualTo(coupons.get(1).getName());
        assertThat(orderDetails.get(2).getCoupon()).isNull();
    }

    @DisplayName("Order 와 OrderDetail 이 서로 연관관계를 맺어야한다")
    @Test
    void mappingOrderWithOrderDetail(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);

        //when
        createOrderService.create(createOrderDto);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderNo(createOrderDto.getOrderNo());
        Order order = orderRepository.findByOrderNo(createOrderDto.getOrderNo());

        //then
        Assertions.assertThat(order.getOrderDetails()).containsExactlyInAnyOrderElementsOf(orderDetails);

        for (OrderDetail orderDetail : orderDetails) {
            assertThat(orderDetail.getOrder()).isEqualTo(order);
        }
    }

    @DisplayName("Order에 최종 쿠폰 사용금액이 저장되어야 한다")
    @Test
    void calcualteTotalUsedCouponPrice(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);
        Long totalCouponPrice = couponRepository.findAll().stream().mapToLong(Coupon::getPrice).sum();


        //when
        createOrderService.create(createOrderDto);
        Order order = orderRepository.findByOrderNo(createOrderDto.getOrderNo());

        //then
        assertThat(order.getTotalUsedCouponPrice()).isEqualTo(totalCouponPrice);
    }

    @DisplayName("MemberCoupon의 사용상태와 사용날짜를 기록해야한다.")
    @Test
    void memberCouponUsage(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAll();

        //when
        createOrderService.create(createOrderDto);

        //then
        assertThat(memberCoupons).hasSize(2);
        assertThat(memberCoupons.get(0).getIsUsed()).isTrue();
        assertThat(memberCoupons.get(1).getIsUsed()).isTrue();
        assertThat(memberCoupons.get(0).getUsedDate()).isNotNull();
        assertThat(memberCoupons.get(1).getUsedDate()).isNotNull();
    }

    @DisplayName("주문 생성시 재고가 감소해야한다.")
    @Test
    void productStockDecrease(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long defaultQuantity = 3L;
        CreateOrderDto createOrderDto = createDto(member, address, defaultQuantity);
        List<Product> products = productRepository.findAll();
        List<Long> stocks = products.stream().map(Product::getStock).toList();

        //when
        createOrderService.create(createOrderDto);
        List<Long> quantities = orderDetailRepository.findAll().stream().map(OrderDetail::getQuantity).toList();

        //then
        assertThat(products).hasSize(3);
        for (int i = 0; i < products.size(); i++) {
            assertThat(products.get(i).getStock()).isEqualTo(stocks.get(i)-quantities.get(i));
        }
    }

    @DisplayName("주문량보다 재고가 많을 경우 예외를 발생시켜야한다")
    @Test
    void productStockDecrease_error(){

        //given
        Member member = memberRepository.findByEmail("hong@email.com").orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByMemberId(member.getId()).get(0);
        Long maxQuantity = 100L;
        CreateOrderDto createOrderDto = createDto(member, address, maxQuantity);

        //when, them
        assertThatThrownBy(() ->
                createOrderService.create(createOrderDto))
                .isInstanceOf(CustomLogicException.class)
                .hasMessageContaining(ErrorCode.OUT_OF_STOCK.getMessage());

    }

    private CreateOrderDto createDto(Member member, Address address, Long lastQuantity) {

        List<Coupon> coupons = couponRepository.findAll();
        List<Product> products = productRepository.findAll();

        ProductValue productValue1 = ProductValue.builder()
                .productId(coupons.get(0).getProduct().getId())
                .couponId(coupons.get(0).getId())
                .quantity(1L)
                .build();

        ProductValue productValue2 = ProductValue.builder()
                .productId(coupons.get(1).getProduct().getId())
                .couponId(coupons.get(1).getId())
                .quantity(2L)
                .build();

        ProductValue productValue3 = ProductValue.builder()
                .productId(products.get(2).getId())
                .quantity(lastQuantity)
                .build();

        List<ProductValue> productValues = List.of(productValue1, productValue2, productValue3);

        Long totalOrderPrice = getTotalOrderPrice(productValues);
        Long totalCouponPrice = getTotalCouponPrice(coupons);

        Long realOrderPrice = totalOrderPrice - totalCouponPrice;

        return CreateOrderDto.builder()
                .memberId(member.getId())
                .addressId(address.getId())
                .orderName("가방 외 2건")
                .orderNo("aaaa-aaaa-aaaa")
                .totalOrderPrice(totalOrderPrice)
                .realOrderPrice(realOrderPrice)
                .productValues(productValues)
                .build();
    }

}