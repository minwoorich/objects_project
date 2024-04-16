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
        private Boolean hasCoupons;
        private Long productGroupId;

        @Builder
        private Response(List<CouponInfo> couponInfos, Boolean hasCoupons, Long productGroupId) {
            this.couponInfos = couponInfos;
            this.hasCoupons = hasCoupons;
            this.productGroupId = productGroupId;
        }
        public static GetCouponHttp.Response create(Boolean hasCoupons, Long productGroupId, List<GetCouponHttp.Response.CouponInfo> couponInfos) {
            return Response.builder()
                    .hasCoupons(hasCoupons)
                    .productGroupId(productGroupId)
                    .couponInfos(hasCoupons ? couponInfos : Collections.emptyList())
                    .build();
        }

        @Getter
        public static class CouponInfo{

            private String couponName;
            private Long couponPrice;
            private Long minimumPrice;
            private Long count;
            private LocalDateTime startDate;
            private LocalDateTime endDate;

            @Builder
            private CouponInfo( String couponName, Long count, Long couponPrice, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
                this.count = count;
                this.couponName = couponName;
                this.couponPrice = couponPrice;
                this.minimumPrice = minimumPrice;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public static GetCouponHttp.Response.CouponInfo of(GetCouponDto dto) {
                return CouponInfo.builder()
                        .couponName(dto.getCouponName())
                        .couponPrice(dto.getPrice())
                        .count(dto.getCount())
                        .minimumPrice(dto.getMinimumPrice())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();
            }
        }
    }
}
