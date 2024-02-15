package com.objects.marketbridge.member.infra.wishlist;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.WishRepository;
import com.objects.marketbridge.product.domain.Option;
import com.objects.marketbridge.product.domain.ProdOption;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import com.objects.marketbridge.product.service.port.OptionRepository;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
//@Transactional
@ActiveProfiles("test")
class WishRepositoryImplTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ProdOptionRepository prodOptionRepository;

    @Autowired
    WishRepository wishRepository;

    @Autowired
    MemberRepository memberRepository;
    @Test
    void findByMemberId() throws Exception{
        //given
        Member member = Member.builder()
                .email("test@naver.com").build();

        memberRepository.save(member);

        Product product = Product.builder()
                .productNo("1")
                .price(100L).build();

        productRepository.save(product);

        Option option = Option.builder()
                .name("Prod1의 option").build();

        optionRepository.save(option);

        ProdOption prodOption = ProdOption.builder()
                .product(product)
                .option(option).build();

        prodOptionRepository.save(prodOption);

        Wishlist wishlist = Wishlist.builder()
                .member(member)
                .productOption(prodOption).build();

        wishRepository.save(wishlist);
        //when
        List<Wishlist> wishlists = wishRepository.findByMemberId(member.getId());

        //then
        Assertions.assertThat(wishlists.size()).isEqualTo(1);
        Assertions.assertThat(wishlists.get(0).getProductOption().getId()).isNotNull();
        Assertions.assertThat(wishlists.get(0).getProductOption().getOption().getName()).isEqualTo("Prod1의 option");
        Assertions.assertThat(wishlists.get(0).getProductOption().getProduct().getPrice()).isEqualTo(100L);
        Assertions.assertThat(wishlists.get(0).getMember().getEmail()).isEqualTo("test@naver.com");
    }

}