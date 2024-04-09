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

        @Builder
        private Response(List<CouponInfo> couponInfos, Boolean hasCoupons) {
            this.couponInfos = couponInfos;
            this.hasCoupons = hasCoupons;
        }
        public static GetCouponHttp.Response create(Boolean hasCoupons, List<GetCouponHttp.Response.CouponInfo> couponInfos) {
            return Response.builder()
                    .hasCoupons(hasCoupons)
                    .couponInfos(hasCoupons ? couponInfos : Collections.emptyList())
                    .build();
        }

        @Getter
        public static class CouponInfo{
            private Long productId;
            private String productNo;
            private String couponName;
            private Long couponPrice;
            private Long minimumPrice;
            private LocalDateTime startDate;
            private LocalDateTime endDate;

            @Builder
            private CouponInfo(Long productId, String productNo, String couponName, Long couponPrice, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
                this.productId = productId;
                this.productNo = productNo;
                this.couponName = couponName;
                this.couponPrice = couponPrice;
                this.minimumPrice = minimumPrice;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public static GetCouponHttp.Response.CouponInfo of(GetCouponDto dto) {
                return CouponInfo.builder()
                        .productId(dto.getProductId())
                        .productNo(dto.getProductNo())
                        .couponName(dto.getCouponName())
                        .couponPrice(dto.getPrice())
                        .minimumPrice(dto.getMinimumPrice())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();
            }
        }
    }
}
