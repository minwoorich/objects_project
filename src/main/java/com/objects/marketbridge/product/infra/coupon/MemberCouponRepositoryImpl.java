package com.objects.marketbridge.product.infra.coupon;

import com.objects.marketbridge.member.domain.MemberCoupon;
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
    public MemberCoupon findByMember_IdAndCoupon_Id(Long memberId, Long couponId) {
        return memberCouponJpaRepository.findByMember_IdAndCoupon_Id(memberId, couponId);
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
