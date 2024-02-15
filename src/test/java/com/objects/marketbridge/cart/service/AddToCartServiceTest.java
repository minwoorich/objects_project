package com.objects.marketbridge.cart.service;


import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.dto.CreateCartDto;
import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
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
        Product outOfStockProduct = Product.create(null, true, "신발", 1000L, false, 1L, "thumbImg1", 0L, "outOfStockProductNo");
        Product addedProduct = Product.create(null, true, "옷", 1000L, false, 100L, "thumbImg1", 0L, "addedProductNo");
        productRepository.saveAll(List.of(product1, outOfStockProduct, addedProduct));

        Member member = Member.create(MembershipType.BASIC.getText(), "test@email.com", "1234", "홍길동", "01012341234", true, true);
        memberRepository.save(member);

        Cart cart = Cart.create(member, addedProduct, false, 2L);
        cartCommendRepository.save(cart);
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

    @DisplayName("이미 장바구니에 담긴 상품일 경우 예외를 발생한다")
    @Test
    void add_validDuplicate(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        CreateCartDto cartDto = CreateCartDto.create("addedProductNo", member.getId(), 2L, false);

        //when
        Throwable thrown = catchThrowable(() -> addToCartService.add(cartDto));

        //then
        assertThat(thrown).isInstanceOf(CustomLogicException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_OPERATION);
    }

    @DisplayName("재고 이상으로 장바구니에 담을경우 예외를 발생한다")
    @Test
    void add_outOfStock(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        CreateCartDto cartDto = CreateCartDto.create("outOfStockProductNo", member.getId(), 2L, false);

        //when
        Throwable thrown = catchThrowable(() -> addToCartService.add(cartDto));

        //then
        assertThat(thrown).isInstanceOf(CustomLogicException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OUT_OF_STOCK);
    }
}