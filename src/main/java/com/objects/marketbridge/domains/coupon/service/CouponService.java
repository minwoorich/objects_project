package com.objects.marketbridge.domains.coupon.service;

import com.objects.marketbridge.domains.coupon.controller.dto.GetCouponHttp;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.dto.GetCouponDto;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    public GetCouponHttp.Response findCouponsForProduct(Long productId) {

        List<Coupon> coupons = couponRepository.findByProductId(productId);
        boolean haveCoupons = !coupons.isEmpty();

        List<GetCouponHttp.Response.CouponInfo> couponInfos = coupons.stream()
                .map(c -> GetCouponDto.of(c, haveCoupons))
                .map(GetCouponHttp.Response.CouponInfo::of)
                .collect(Collectors.toList());

        return GetCouponHttp.Response.create(haveCoupons, haveCoupons ? couponInfos : Collections.emptyList());
    }
}
