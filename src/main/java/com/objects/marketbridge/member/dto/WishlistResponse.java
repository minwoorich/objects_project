package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.member.domain.Wishlist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistResponse {

    private Long prodOptionId;
    private String prodOptionName;
    private Long price;
    private String productName;
    private String thumbImgUrl;
    private Boolean isSoldOut;

    @Builder
    public WishlistResponse(Long prodOptionId, String prodOptionName, Long price, String productName, String thumbImgUrl, Boolean isSoldOut) {
        this.prodOptionId = prodOptionId;
        this.prodOptionName = prodOptionName;
        this.price = price;
        this.productName = productName;
        this.thumbImgUrl = thumbImgUrl;
        this.isSoldOut = isSoldOut;
    }

    public static WishlistResponse of(Wishlist wishlist){
        return  WishlistResponse.builder()
                .prodOptionId(wishlist.getProductOption().getId())
                .prodOptionName(wishlist.getProductOption().getOption().getName())
                .price(wishlist.getProductOption().getProduct().getPrice())
                .productName(wishlist.getProductOption().getProduct().getName())
                .thumbImgUrl(wishlist.getProductOption().getProduct().getThumbImg())
                .isSoldOut(wishlist.getProductOption().getProduct().getStock() <= 0)
                .build();
    }
}
