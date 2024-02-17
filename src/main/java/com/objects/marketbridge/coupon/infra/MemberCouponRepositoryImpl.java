package com.objects.marketbridge.coupon.infra;

import com.objects.marketbridge.coupon.domain.MemberCoupon;
import com.objects.marketbridge.coupon.service.port.MemberCouponRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return memberCouponJpaRepository.findByMemberIdAndCouponId(memberId, couponId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberCoupon save(MemberCoupon memberCoupon) {
        return memberCouponJpaRepository.save(memberCoupon);
    }

    @Override
    public List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons) {
        return memberCouponJpaRepository.saveAll(memberCoupons);
    }

    @Override
    public List<MemberCoupon> findAll() {
        return memberCouponJpaRepository.findAll();
    }
}
