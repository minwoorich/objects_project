package com.objects.marketbridge.domains.coupon.controller;

import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.domains.coupon.controller.dto.GetCouponHttp;
import com.objects.marketbridge.domains.coupon.service.CouponService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CouponController {

    private final CouponService couponService;

    @Builder
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons/{productGroupId}")
    @UserAuthorize
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<GetCouponHttp.Response> findCouponsForProductGroup(
            @PathVariable Long productGroupId) {

        return ApiResponse.ok(couponService.findCouponsForProductGroup(productGroupId));
    }
}
