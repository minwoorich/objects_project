package com.objects.marketbridge.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request{

        private String productNo;
        private String thumbImageUrl;
        private String productName;
        private String productOptionName;
        private String deliveredDate; // 예상 도착시간(yyyy.MM.dd)
        private Long quantity;
        private Long discountRate;
        private Boolean isCouponAvailable;
        private Long couponNo;
        private Boolean isOwn;
        private Boolean isSubs;

        @Builder
        private Request(String productNo, String thumbImageUrl, String productName, String productOptionName, String deliveredDate, Long quantity, Long discountRate, Boolean isCouponAvailable, Boolean isOwn, Boolean isSubs) {
            this.productNo = productNo;
            this.thumbImageUrl = thumbImageUrl;
            this.productName = productName;
            this.productOptionName = productOptionName;
            this.deliveredDate = deliveredDate;
            this.quantity = quantity;
            this.discountRate = discountRate;
            this.isCouponAvailable = isCouponAvailable;
            this.isOwn = isOwn;
            this.isSubs = isSubs;
        }

        public static Request create(String productNo, String thumbImageUrl, String productName, String productOptionName, String deliveredDate, Long quantity, Long discountRate, Boolean isCouponAvailable, Boolean isOwn, Boolean isSubs) {
            return Request.builder()
                    .productNo(productNo)
                    .thumbImageUrl(thumbImageUrl)
                    .productName(productName)
                    .productOptionName(productOptionName)
                    .deliveredDate(deliveredDate)
                    .quantity(quantity)
                    .discountRate(discountRate)
                    .isCouponAvailable(isCouponAvailable)
                    .isOwn(isOwn)
                    .isSubs(isSubs)
                    .build();
        }
    }
}
