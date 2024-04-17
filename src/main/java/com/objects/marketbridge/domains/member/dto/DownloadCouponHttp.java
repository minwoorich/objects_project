package com.objects.marketbridge.domains.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class DownloadCouponHttp {

    @Getter
    public static class Request{
        @NotNull
        private Long couponId;
        @NotNull
        private Long productGroupId;

        @Builder
        private Request(Long couponId, Long productGroupId) {
            this.couponId = couponId;
            this.productGroupId = productGroupId;
        }
    }
    @Getter
    public static class Response {


    }
}
