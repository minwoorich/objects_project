package com.objects.marketbridge.domains.member.service;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.mock.BaseFakeCouponRepository;
import com.objects.marketbridge.domains.coupon.mock.FakeCouponRepository;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.mock.FakeMemberRepository;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class RegisterCouponServiceTest {

    CouponRepository couponRepository = new FakeCouponRepository();
    MemberRepository memberRepository = new FakeMemberRepository();

    RegisterCouponService registerCouponService = RegisterCouponService.builder()
            .couponRepository(couponRepository)
            .memberRepository(memberRepository)
            .build();

    @AfterEach
    void clear() {
        BaseFakeCouponRepository.getInstance().clear();
        memberRepository.deleteAllInBatch();
    }
    @Test
    @DisplayName("회원이 쿠폰을 다운로드 받을 수 있다")
    void registerCouponToMember() {
        // given
        Coupon coupon = couponRepository.save(Coupon.builder().price(1000L).count(100L).build());
        Member member = memberRepository.save(Member.builder().name("홍길동").build());

        // when
        Coupon findCoupon = registerCouponService.registerCouponToMember(member.getId(), coupon.getId());

        // then
        assertThat(findCoupon.getMemberCoupons())
                .extracting(
                        mc -> mc.getMember(),
                        mc -> mc.getCoupon().getCount(),
                        mc -> mc.getCoupon().getPrice())
                .contains(
                        Tuple.tuple(member, 99L, 1000L));
    }

    @DisplayName("쿠폰 수량이 모두 소진 된 경우 에러를 발생시킨다")
    @Test
    void registerCouponToMember_error(){
        //given
        Coupon coupon = couponRepository.save(Coupon.builder().price(1000L).count(0L).build());
        Member member = memberRepository.save(Member.builder().name("홍길동").build());

        //when
        Throwable thrown = catchThrowable(() -> registerCouponService.registerCouponToMember(member.getId(), coupon.getId()));

        //then
        assertThat(thrown)
                .isInstanceOf(CustomLogicException.class)
                .hasMessage(ErrorCode.COUPON_OUT_OF_STOCK.getMessage());
    }
}