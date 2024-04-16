package com.objects.marketbridge.domains.coupon.service.dto;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCouponDto {
    private Boolean hasCoupons;
    private List<MemberCoupon> memberCoupons;
    private Long productGroupId;
    private String couponName;
    private Long price;
    private Long count;
    private Long minimumPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    private GetCouponDto(Boolean hasCoupons, List<MemberCoupon> memberCoupons, String couponName, Long productGroupId, Long price, Long count, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.hasCoupons = hasCoupons;
        this.memberCoupons = memberCoupons;
        this.couponName = couponName;
        this.productGroupId = productGroupId;
        this.price = price;
        this.count = count;
        this.minimumPrice = minimumPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static GetCouponDto of(Coupon coupon, Boolean hasCoupons) {
        return GetCouponDto.builder()
                .memberCoupons(coupon.getMemberCoupons())
                .hasCoupons(hasCoupons)
                .productGroupId(coupon.getProductGroupId())
                .couponName(coupon.getName())
                .price(coupon.getPrice())
                .count(coupon.getCount())
                .minimumPrice(coupon.getMinimumPrice())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .build();
    }
}
