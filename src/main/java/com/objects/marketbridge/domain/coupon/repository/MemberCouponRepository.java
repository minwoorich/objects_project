package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.model.MemberCoupon;

public interface MemberCouponRepository {
    MemberCoupon findById(Long id);

    MemberCoupon findByMember_IdAndCoupon_Id(Long MemberId, Long CouponId);

    MemberCoupon save(MemberCoupon memberCoupon);
}
