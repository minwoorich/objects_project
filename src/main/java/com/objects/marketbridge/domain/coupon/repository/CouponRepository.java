package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.domain.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long id);

    List<Coupon> findAllByIds(List<Long> ids);

    void save(Coupon coupon);
}
