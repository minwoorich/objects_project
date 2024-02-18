package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.coupon.domain.Coupon;
import com.objects.marketbridge.coupon.service.port.CouponRepository;

import java.util.List;

public class FakeCouponRepository implements CouponRepository {
    @Override
    public Coupon findById(Long id) {
        return null;
    }


    @Override
    public void save(Coupon coupon) {

    }

    @Override
    public void saveAll(List<Coupon> coupons) {

    }

    @Override
    public List<Coupon> findAll() {
        return null;
    }
}
