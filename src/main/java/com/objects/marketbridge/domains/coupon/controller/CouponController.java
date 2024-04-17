package com.objects.marketbridge.domains.coupon.controller;

import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.domains.coupon.controller.dto.GetCouponHttp;
import com.objects.marketbridge.domains.coupon.service.GetCouponService;
import com.objects.marketbridge.domains.coupon.service.dto.GetCouponDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CouponController {

    private final GetCouponService getCouponService;

    @Builder
    public CouponController(GetCouponService getCouponService) {
        this.getCouponService = getCouponService;
    }

    @GetMapping("/coupons/{productGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetCouponHttp.Response> findCouponsForProductGroup(
            @PathVariable Long productGroupId,
            @AuthMemberId Long memberId) {

        return ApiResponse.ok(convertToResponse(getCouponService.findCouponsForProductGroup(productGroupId, memberId)));
    }

    private GetCouponHttp.Response convertToResponse(List<GetCouponDto> couponDtos) {

        if (couponDtos.isEmpty()) {
            return GetCouponHttp.Response.create();
        }
        List<GetCouponHttp.Response.CouponInfo> couponInfos = couponDtos.stream()
                        .map(GetCouponHttp.Response.CouponInfo::of) // GetCouponDto -> GetCouponHttp.Response.CouponInfo
                        .collect(Collectors.toList());
        return GetCouponHttp.Response.create(couponInfos);
    }
}
