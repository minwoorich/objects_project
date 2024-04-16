package com.objects.marketbridge.domains.coupon.service;

import com.objects.marketbridge.domains.coupon.controller.dto.GetCouponHttp;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.dto.GetCouponDto;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    @Builder
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public GetCouponHttp.Response findCouponsForProductGroup(Long productGroupId) {

        List<Coupon> coupons = couponRepository.findByProductGroupId(productGroupId);
        boolean hasCoupons = !coupons.isEmpty();

        List<GetCouponHttp.Response.CouponInfo> couponInfos = coupons.stream()
                .map(c -> GetCouponDto.of(c, hasCoupons))// Coupon -> GetCouponDto
                .map(GetCouponHttp.Response.CouponInfo::of) // CouponDto -> GetCouponHttp.Response.CouponInfo
                .collect(Collectors.toList());

        return GetCouponHttp.Response.create(hasCoupons, productGroupId, hasCoupons ? couponInfos : Collections.emptyList());
    }

    public GetCouponHttp.Response findCouponsForProductGroup(Long productGroupId, Long memberId) {

        List<Coupon> coupons = couponRepository.findByProductGroupIdWithMemberCoupons(productGroupId);
        boolean hasCoupons = !coupons.isEmpty();

        List<GetCouponHttp.Response.CouponInfo> couponInfos = coupons.stream()
                .map(c -> GetCouponDto.of(c, hasCoupons))// Coupon -> GetCouponDto
                .map(GetCouponHttp.Response.CouponInfo::of) // CouponDto -> GetCouponHttp.Response.CouponInfo
                .collect(Collectors.toList());
//        coupons.stream().filter(c -> c.getMemberCoupons())

        return GetCouponHttp.Response.create(hasCoupons, productGroupId, hasCoupons ? couponInfos : Collections.emptyList());
    }
}
