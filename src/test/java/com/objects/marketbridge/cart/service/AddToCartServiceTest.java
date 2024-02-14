package com.objects.marketbridge.cart.service;


import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.dto.CreateCartDto;
import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AddToCartServiceTest {
    @Autowired CartCommendRepository cartCommendRepository;
    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ProductRepository productRepository;
    @Autowired AddToCartService addToCartService;
    @BeforeEach
    void init() {

        Product product1 = Product.create(null, true, "가방", 1000L, false, 100L, "thumbImg1", 0L, "productNo1");
        Product product2 = Product.create(null, true, "신발", 1000L, false, 100L, "thumbImg1", 0L, "productNo2");
        Product product3 = Product.create(null, true, "옷", 1000L, false, 100L, "thumbImg1", 0L, "productNo3");
        productRepository.saveAll(List.of(product1, product2, product3));

        Member member = Member.create(MembershipType.BASIC.getText(), "test@email.com", "1234", "홍길동", "01012341234", true, true);
        memberRepository.save(member);
    }
    @AfterEach
    void clear() {
        cartCommendRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("장바구니에 상품을 담을 수 있다")
    @Test
    void add(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        CreateCartDto cartDto = CreateCartDto.create("productNo1", member.getId(), 2L, false);

        //when
        Cart cart = addToCartService.add(cartDto);

        //then
        assertThat(cart.getMember().getId()).isEqualTo(cartDto.getMemberId());
        assertThat(cart.getProduct().getProductNo()).isEqualTo(cartDto.getProductNo());
    }

}