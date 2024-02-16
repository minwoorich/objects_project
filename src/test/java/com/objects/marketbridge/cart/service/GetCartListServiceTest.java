package com.objects.marketbridge.cart.service;


import com.objects.marketbridge.cart.controller.dto.GetCartListHttp;
import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.dto.GetCartDto;
import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.cart.service.port.CartDtoRepository;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.common.interceptor.SliceResponse;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.domain.Option;
import com.objects.marketbridge.product.domain.ProdOption;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import com.objects.marketbridge.product.service.port.OptionRepository;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GetCartListServiceTest {

    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired CartDtoRepository cartDtoRepository;
    @Autowired CartCommendRepository cartCommendRepository;
    @Autowired ProductRepository productRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired GetCartListService getCartListService;
    @Autowired OptionRepository optionRepository;
    @Autowired ProdOptionRepository prodOptionRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);

        Option option1 = Option.builder().name("옵션1").build();
        Option option2 = Option.builder().name("옵션2").build();
        optionRepository.saveAll(List.of(option1, option2));


        ProdOption prodOption1_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption1_2 = ProdOption.builder().option(option2).build();
        ProdOption prodOption2_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption2_2 = ProdOption.builder().option(option2).build();
        ProdOption prodOption3_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption3_2 = ProdOption.builder().option(option2).build();
        ProdOption prodOption4_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption4_2 = ProdOption.builder().option(option2).build();
        ProdOption prodOption5_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption5_2 = ProdOption.builder().option(option2).build();
        ProdOption prodOption6_1 = ProdOption.builder().option(option1).build();
        ProdOption prodOption6_2 = ProdOption.builder().option(option2).build();

        Product product1 = Product.builder().stock(5L).productNo("productNo1").build();
        Product product2 = Product.builder().stock(5L).productNo("productNo2").build();
        Product product3 = Product.builder().stock(5L).productNo("productNo3").build();
        Product product4 = Product.builder().stock(5L).productNo("productNo4").build();
        Product product5 = Product.builder().stock(5L).productNo("productNo5").build();
        Product product6 = Product.builder().stock(5L).productNo("productNo6").build();

        product1.addProdOptions(prodOption1_1);
        product1.addProdOptions(prodOption1_2);

        product2.addProdOptions(prodOption2_1);
        product2.addProdOptions(prodOption2_2);

        product3.addProdOptions(prodOption3_1);
        product3.addProdOptions(prodOption3_2);

        product4.addProdOptions(prodOption4_1);
        product4.addProdOptions(prodOption4_2);

        product5.addProdOptions(prodOption5_1);
        product5.addProdOptions(prodOption5_2);

        product6.addProdOptions(prodOption6_1);
        product6.addProdOptions(prodOption6_2);
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5, product6));



        Cart cart1 = Cart.create(member, product1, false, 1L);
        Cart cart2 = Cart.create(member, product2, false, 1L);
        Cart cart3 = Cart.create(member, product3, false, 1L);
        Cart cart4 = Cart.create(member, product4, false, 1L);
        Cart cart5 = Cart.create(member, product5, false, 1L);
        Cart cart6 = Cart.create(member, product6, false, 1L);
        cartCommendRepository.saveAll(List.of(cart1, cart2, cart3, cart4, cart5, cart6));
    }

    @AfterEach
    void clear() {
        cartCommendRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        optionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }


    @DisplayName("SliceResponse 로 변환된다")
    @Test
    void get_SliceResponse(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        GetCartListHttp.Response response = getCartListService.get(pageRequest, member.getId());

        //then
        assertThat(response.getCartItems()).isInstanceOf(SliceResponse.class);
        assertThat(response.getCartItems().getSize()).isEqualTo(pageSize);
        assertThat(response.getCartItems().getSort().getDirection()).isEqualTo(Sort.Direction.DESC.toString());
        assertThat(response.getCartItems().getSort().getOrderProperty()).isEqualTo("createdAt");
        assertThat(response.getCartItems().getCurrentPage()).isEqualTo(0);
        assertThat(response.getCartItems().isFirst()).isTrue();
        assertThat(response.getCartItems().isLast()).isFalse();
        assertThat(response.getCartItems().getContent()).hasSize(pageSize);
    }

    @DisplayName("장바구니를 조회할 수 있다")
    @Test
    void get(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        GetCartListHttp.Response response = getCartListService.get(pageRequest, member.getId());

        //then
        // response.getCartItems().getContent()의 각 요소가 GetCartDto 클래스의 인스턴스인지 확인
        assertThat(response.getCartItems().getContent())
                .allSatisfy(cartItem -> assertThat(cartItem).isInstanceOf(GetCartDto.class));

        // 각각의 CartInfo 객체가 2개의 옵션을 가지고 있으며 각 옵션 이름은 '옵션1', '옵션2' 여야 한다.
        assertThat(response.getCartItems().getContent())
                .extracting(GetCartDto::getOptionNames)
                .as("OptionNames의 크기는 2여야 합니다.")
                .allMatch(optionNames -> optionNames.size() == 2)
                .allSatisfy(optionNames -> {
                    assertThat(optionNames).containsExactly("옵션1", "옵션2");
                });
    }
}