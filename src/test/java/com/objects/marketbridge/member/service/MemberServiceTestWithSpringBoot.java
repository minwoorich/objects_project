package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.dto.WishlistRequest;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.WishRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.service.port.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTestWithSpringBoot {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WishRepository wishRepository;

    @Autowired
    MemberService memberService;


    @Test
    @DisplayName("wishlist에 추가")
    public void addWishlist(){
        //given
        Product product= Product.builder()
                .stock(1L)
                .name("Product Name")
                .thumbImg("image/static")
                .price(1000L)
                .isOwn(false)
                .build();

        productRepository.save(product);


        Member member = Member.builder()
                .email("WishTest@naver.com").build();

        memberRepository.save(member);

        WishlistRequest request = WishlistRequest.builder()
                .productId(product.getId())
                .build();
        //when
        memberService.addWish(member.getId(),request);
        Wishlist findWishlist = wishRepository.findByMemberId(member.getId()).get(0);
        //then
        Assertions.assertThat(findWishlist.getProduct().getName()).isEqualTo("Product Name");
        Assertions.assertThat(findWishlist.getMember().getEmail()).isEqualTo("WishTest@naver.com");

    }
}