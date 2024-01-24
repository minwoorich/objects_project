package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.model.MemberCoupon;

public interface MemberCouponRepository {
    MemberCoupon findById(Long id);

    MemberCoupon findByMemberIdAndCouponId(Long MemberId, Long CouponId);
}
