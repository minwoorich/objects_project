package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.model.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberCouponJpaRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("SELECT mc FROM MemberCoupon mc WHERE mc.member.id = :memberId AND mc.coupon.id = :couponId")
    MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId);
}
