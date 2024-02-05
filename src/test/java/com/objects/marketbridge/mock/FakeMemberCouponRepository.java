package com.objects.marketbridge.mock;

import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.infra.MemberCouponRepository;

import java.util.List;

public class FakeMemberCouponRepository implements MemberCouponRepository {
    @Override
    public MemberCoupon findById(Long id) {
        return null;
    }

    @Override
    public MemberCoupon findByMember_IdAndCoupon_Id(Long MemberId, Long CouponId) {
        return null;
    }

    @Override
    public MemberCoupon save(MemberCoupon memberCoupon) {
        return null;
    }

    @Override
    public List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons) {
        return null;
    }

    @Override
    public List<MemberCoupon> findAll() {
        return null;
    }
}
