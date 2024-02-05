package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCouponJpaRepository extends JpaRepository<MemberCoupon, Long> {

    @Query("SELECT mc FROM MemberCoupon mc WHERE mc.member.id = :memberId AND mc.coupon.id = :couponId")
    MemberCoupon findByMember_IdAndCoupon_Id(@Param("memberId") Long memberId, @Param("couponId") Long couponId);
}
