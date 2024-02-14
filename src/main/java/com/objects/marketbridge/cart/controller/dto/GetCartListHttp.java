package com.objects.marketbridge.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetCartListHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        List<ProductInfo> productInfos;

        @Builder
        private Response(List<ProductInfo> productInfos) {
            this.productInfos = productInfos;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfo{

        private String productNo;
        private String productName;
        private String productOptionName;
        private Long productPrice;
        private Long quantity;
        private Long discountRate;
        private String thumbImageUrl;
        private Boolean isOwn;
        private Boolean isSubs;
        private Long couponPrice;
        private String couponName;
        private Long couponMinimumPrice;
        private String couponEndDate; // yyyy-MM-dd HH:mm:ss
        private Long stock;
        private Long deliveryFee;
        private String deliveredDate; // yyyy.MM.dd

        @Builder
        private ProductInfo(String productNo, String productName, String productOptionName, Long productPrice, Long quantity, Long stock, Long discountRate, String thumbImageUrl, Boolean isOwn, Boolean isSubs, Long couponPrice, String couponName, Long couponMinimumPrice, String couponEndDate,  Long deliveryFee, String deliveredDate) {
            this.productNo = productNo;
            this.productName = productName;
            this.productOptionName = productOptionName;
            this.productPrice = productPrice;
            this.quantity = quantity;
            this.stock = stock;
            this.discountRate = discountRate;
            this.thumbImageUrl = thumbImageUrl;
            this.isOwn = isOwn;
            this.isSubs = isSubs;
            this.couponPrice = couponPrice;
            this.couponName = couponName;
            this.couponMinimumPrice = couponMinimumPrice;
            this.couponEndDate = couponEndDate;
            this.deliveryFee = deliveryFee;
            this.deliveredDate = deliveredDate;
        }

        public static ProductInfo create(String productNo, String productName, String productOptionName, Long productPrice, Long quantity, Long stock, Long discountRate, String thumbImageUrl, Boolean isOwn, Boolean isSubs, Long couponPrice, String couponName, Long couponMinimumPrice, String couponEndDate, Long deliveryFee, String deliveredDate) {
            return ProductInfo.builder()
                    .productNo(productNo)
                    .productName(productName)
                    .productOptionName(productOptionName)
                    .productPrice(productPrice)
                    .quantity(quantity)
                    .stock(stock)
                    .discountRate(discountRate)
                    .thumbImageUrl(thumbImageUrl)
                    .isOwn(isOwn)
                    .isSubs(isSubs)
                    .couponPrice(couponPrice)
                    .couponName(couponName)
                    .couponMinimumPrice(couponMinimumPrice)
                    .couponEndDate(couponEndDate)
                    .deliveryFee(deliveryFee)
                    .deliveredDate(deliveredDate)
                    .build();

        }
    }
}
