package com.objects.marketbridge.domain.coupon.repository;

import com.objects.marketbridge.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

}
