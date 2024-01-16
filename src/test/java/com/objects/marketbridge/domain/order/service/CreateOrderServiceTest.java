package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class CreateOrderServiceTest {

    @Autowired CreateOrderService createOrderService;
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

}