package com.objects.marketbridge.cart.infra;

import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class CartQueryRepositoryImplTest {

    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired CartCommendRepository cartCommendRepository;
    @Autowired ProductRepository productRepository;
    @Autowired MemberRepository memberRepository;

    @DisplayName("상품 번호를 통해 카트를 조회할 수 있다")
    @Test
    void findByProductNo(){
        //given
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);

        Product product1 = Product.builder().stock(5L).productNo("productNo1").build();
        Product product2 = Product.builder().stock(5L).productNo("productNo2").build();
        productRepository.saveAll(List.of(product1, product2));

        Cart cart1 = Cart.create(member, product1, false, 1L);
        Cart cart2 = Cart.create(member, product2, false, 1L);
        cartCommendRepository.saveAll(List.of(cart1, cart2));

        //when
        Optional<Cart> findProduct1 = cartQueryRepository.findByProductNo("productNo1");
        Optional<Cart> findProduct2 = cartQueryRepository.findByProductNo("productNo2");

        //then
        Assertions.assertThat(findProduct1.isPresent()).isTrue();
        Assertions.assertThat(findProduct2.isPresent()).isTrue();
    }
}