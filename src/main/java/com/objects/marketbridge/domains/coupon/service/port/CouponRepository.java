package com.objects.marketbridge.domains.coupon.service.port;

import com.objects.marketbridge.domains.coupon.domain.Coupon;

import java.util.List;

public interface CouponRepository {
    Coupon findById(Long id);

    List<Coupon> findByProductId(Long productId);

    List<Coupon> findByProductGroupId(Long productGroupId);

    Coupon save(Coupon coupon);

    void saveAll(List<Coupon> coupons);

    List<Coupon> findAll();

    void deleteAllInBatch();

    Coupon saveAndFlush(Coupon coupon);
}
