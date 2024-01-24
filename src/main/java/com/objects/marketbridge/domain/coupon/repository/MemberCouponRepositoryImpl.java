package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.model.MemberCoupon;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponRepository {

    private final MemberCouponJpaRepository memberCouponJpaRepository;

    @Override
    public MemberCoupon findById(Long id) {
        return memberCouponJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId) {
        return memberCouponJpaRepository.findByMemberIdAndCouponId(memberId, couponId);
    }
}
