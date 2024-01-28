package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.order.service.CouponUsageService;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.member.infra.MemberRepository;
import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.MemberCoupon;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CouponUsageServiceTest {

    @Autowired
    MemberCouponRepository memberCouponRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired
    CouponUsageService couponUsageService;

    @BeforeEach
    void init() {
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);

        List<Coupon> coupons = createCoupons();
        couponRepository.saveAll(coupons);

        List<MemberCoupon> memberCoupons = createMemberCoupons(member, coupons);
        memberCouponRepository.saveAll(memberCoupons);
    }


    @Test
    @DisplayName("사용된 쿠폰의 사용 상태와 사용 날짜를 저장한다")
    void applyCouponUsage() {

        //given
        LocalDateTime dateTime = LocalDateTime.now();

        List<Coupon> coupons = couponRepository.findAll();
        Long memberId = memberRepository.findByEmail("test@email.com").orElseThrow(EntityNotFoundException::new).getId();

        List<MemberCoupon> memberCoupons = getMemberCoupons(coupons, memberId);

        //when
        couponUsageService.applyCouponUsage(memberCoupons, true, dateTime);

        //then
        for (MemberCoupon mc : memberCoupons) {
            Assertions.assertThat(mc.getIsUsed()).isTrue();
            Assertions.assertThat(mc.getUsedDate()).isEqualTo(dateTime);
        }
    }

    private List<MemberCoupon> getMemberCoupons(List<Coupon> coupons, Long memberId) {
        return coupons.stream()
                .map(c ->
                        memberCouponRepository.findByMember_IdAndCoupon_Id(memberId, c.getId()))
                .toList();
    }

    private List<Coupon> createCoupons() {

        Coupon coupon1 = Coupon.builder().name("1000원짜리 쿠폰").build();
        Coupon coupon2 = Coupon.builder().name("2000원짜리 쿠폰").build();
        Coupon coupon3 = Coupon.builder().name("3000원짜리 쿠폰").build();

        return List.of(coupon1, coupon2, coupon3);
    }

    private List<MemberCoupon> createMemberCoupons(Member member, List<Coupon> coupons) {
        MemberCoupon memberCoupon1 = MemberCoupon.builder().member(member).coupon(coupons.get(0)).build();
        MemberCoupon memberCoupon2 = MemberCoupon.builder().member(member).coupon(coupons.get(1)).build();
        MemberCoupon memberCoupon3 = MemberCoupon.builder().member(member).coupon(coupons.get(2)).build();

        return List.of(memberCoupon1, memberCoupon2, memberCoupon3);
    }
}