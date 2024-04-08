package com.objects.marketbridge.domains.coupon.controller.dto;

import com.objects.marketbridge.domains.coupon.service.dto.GetCouponDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class GetCouponHttp {

    @Getter
    public static class Response{
        private List<CouponInfo> couponInfos;
        private Boolean haveCoupons;

        @Builder
        private Response(List<CouponInfo> couponInfos, Boolean haveCoupons) {
            this.couponInfos = couponInfos;
            this.haveCoupons = haveCoupons;
        }
        public static GetCouponHttp.Response create(Boolean haveCoupons, List<GetCouponHttp.Response.CouponInfo> couponInfos) {
            return Response.builder()
                    .haveCoupons(haveCoupons)
                    .couponInfos(haveCoupons ? couponInfos : Collections.emptyList())
                    .build();
        }

        @Getter
        public static class CouponInfo{
            private Long productId;
            private String productNo;
            private String couponName;
            private Long price;
            private Long minimumPrice;
            private LocalDateTime startDate;
            private LocalDateTime endDate;

            @Builder
            private CouponInfo(Long productId, String productNo, String couponName, Long price, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
                this.productId = productId;
                this.productNo = productNo;
                this.couponName = couponName;
                this.price = price;
                this.minimumPrice = minimumPrice;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public static GetCouponHttp.Response.CouponInfo of(GetCouponDto dto) {
                return CouponInfo.builder()
                        .productId(dto.getProductId())
                        .productNo(dto.getProductNo())
                        .couponName(dto.getCouponName())
                        .price(dto.getPrice())
                        .minimumPrice(dto.getMinimumPrice())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();
            }
        }
    }
}
