package com.objects.marketbridge.domains.coupon.service;

import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class DownloadCouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Builder
    public DownloadCouponService(MemberRepository memberRepository, CouponRepository couponRepository) {
        this.memberRepository = memberRepository;
        this.couponRepository = couponRepository;
    }

    public void registerCouponToMember(Long memberId, Long couponId) {
        Member member = memberRepository.findById(memberId);
    }
}
