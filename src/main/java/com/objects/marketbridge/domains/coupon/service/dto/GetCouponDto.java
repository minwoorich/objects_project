package com.objects.marketbridge.domains.coupon.service.dto;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetCouponDto {
    private Boolean hasCoupons;
    private Long productId;
    private String productNo;
    private String couponName;
    private Long price;
    private Long count;
    private Long minimumPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    private GetCouponDto(Boolean hasCoupons, Long productId, String productNo, String couponName, Long price, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.hasCoupons = hasCoupons;
        this.productId = productId;
        this.productNo = productNo;
        this.couponName = couponName;
        this.price = price;
        this.minimumPrice = minimumPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static GetCouponDto of(Coupon coupon, Boolean hasCoupons) {
        return GetCouponDto.builder()
                .hasCoupons(hasCoupons)
                .productId(coupon.getProduct().getId())
                .productNo(coupon.getProduct().getProductNo())
                .couponName(coupon.getName())
                .price(coupon.getPrice())
                .minimumPrice(coupon.getMinimumPrice())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .build();
    }
}