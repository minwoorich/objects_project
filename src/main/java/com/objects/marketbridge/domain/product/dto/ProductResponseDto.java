package com.objects.marketbridge.domain.product.dto;

import com.objects.marketbridge.domain.model.Category;
import lombok.Builder;

public class ProductResponseDto {

    private Category categoryId;
    private Boolean isOwn; // 로켓 true , 오픈 마켓 false
    private String name;
    private Long price;
    private Boolean isSubs;
    private String thumbImg;
    private Long discountRate;

    @Builder
    public ProductResponseDto(Category categoryId, boolean isOwn, String name, Long price, boolean isSubs, String thumbImg, Long discountRate) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
    }

    //에러나서 기본생성자 임시로 만듦.
    public ProductResponseDto() {
    }
}
