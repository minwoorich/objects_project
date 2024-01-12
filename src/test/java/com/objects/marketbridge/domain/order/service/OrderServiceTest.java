package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.dto.ProductInfoDto;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.product.repository.ProductJpaRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired ProductRepository productRepository;
    @Autowired ProductJpaRepository productJpaRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired AddressRepository addressRepository;
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired OrderRepository orderRepository;

    @BeforeEach
    void init(){
        // member 생성
        Member member = Member.builder()
                .email("hong@email.com")
                .name("홍길동").build();
        memberRepository.save(member);

        // address 생성
        Address address = Address.builder()
                .member(member).build();
        addressRepository.save(address);

        // product 생성
        Product product1 = Product.builder()
                .name("가방")
                .price(1000L)
                .build();
        Product product2 = Product.builder()
                .name("티비")
                .price(2000L)
                .build();
        Product product3 = Product.builder()
                .name("워치")
                .price(3000L)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));
    }
    @AfterEach
    void tearDown() {
        productJpaRepository.deleteAllInBatch();
        addressRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        orderDetailRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("물건을 N가지 상품을 샀으면 받았으면 N개의 주문이 생성된다.")
    @Test
    void create(){
        //given
        Member findMember = memberRepository.findByEmail("hong@email.com").orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지않습니다"));
        Address findAddress = addressRepository.findByMemberId(findMember.getId());
        Long findProductId1 = productRepository.findByName("가방").get(0).getId();
        Long findProductId2 = productRepository.findByName("티비").get(0).getId();
        Long findProductId3 = productRepository.findByName("워치").get(0).getId();

        ProductInfoDto productInfoDto1 = ProductInfoDto.builder()
                .productId(findProductId1)
                .quantity(1L)
                .unitOrderPrice(1000L)
                .build();

        ProductInfoDto productInfoDto2 = ProductInfoDto.builder()
                .productId(findProductId2)
                .quantity(2L)
                .unitOrderPrice(4000L)
                .build();

        ProductInfoDto productInfoDto3 = ProductInfoDto.builder()
                .productId(findProductId3)
                .quantity(3L)
                .unitOrderPrice(9000L)
                .build();

        List<ProductInfoDto> productInfos = List.of(productInfoDto1, productInfoDto2, productInfoDto3);

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .payMethod("신용카드")
                .orderName("가방 외 2건")
                .totalOrderPrice(14000L)
                .addressId(findAddress.getId())
                .productInfos(productInfos)
                .build();

        CreateProdOrderDto prodOrderDto = createOrderRequest.toProdOrderDto(findMember.getId());
        List<CreateProdOrderDetailDto> prodOrderDetailDtos = createOrderRequest.toProdOrderDetailDtos();

        //when
        orderService.create(prodOrderDto, prodOrderDetailDtos);
        List<ProdOrderDetail> allOrders = orderDetailRepository.findAll();
        //then
        assertThat(allOrders).hasSize(3);
        assertThat(allOrders)
                .map(o -> o.getProduct().getName())
                .containsExactlyInAnyOrderElementsOf(List.of("가방", "티비", "워치"));
    }
}