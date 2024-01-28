package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.member.infra.MemberRepository;
import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.MemberCoupon;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberCouponRepositoryImplTest {

    @Autowired
    MemberCouponRepository memberCouponRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired
    CouponRepository couponRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);
        Coupon coupon = Coupon.builder().name("5000원짜리 쿠폰").build();
        couponRepository.save(coupon);
        MemberCoupon memberCoupon = MemberCoupon.builder().member(member).coupon(coupon).build();
        memberCouponRepository.save(memberCoupon);
    }

    @DisplayName("멤버아이디와 쿠폰아이디로 멤버쿠폰을 가져올수 있다.")
    @Test
    void findByMember_IdAndCoupon_Id(){
        //given
        Member member = memberRepository.findByEmail("test@email.com").orElseThrow(EntityNotFoundException::new);
        Coupon coupon = couponRepository.findAll().get(0);

        //when
        MemberCoupon memberCoupon = memberCouponRepository.findByMember_IdAndCoupon_Id(member.getId(), coupon.getId());

        //then
        Assertions.assertThat(memberCoupon.getMember().getEmail()).isEqualTo("test@email.com");
        Assertions.assertThat(memberCoupon.getCoupon().getName()).isEqualTo("5000원짜리 쿠폰");
    }
}