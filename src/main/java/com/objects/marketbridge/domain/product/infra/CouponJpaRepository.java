package com.objects.marketbridge.domain.product.infra;

import com.objects.marketbridge.common.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByIdIn(List<Long> ids);
}
