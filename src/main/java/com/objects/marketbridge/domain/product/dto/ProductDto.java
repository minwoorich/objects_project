package com.objects.marketbridge.domain.product.dto;

import com.objects.marketbridge.domain.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductDto {

    private Category categoryId;
    private boolean isOwn; // 로켓 true , 오픈 마켓 false
    private String name;
    private Long price;
    private boolean isSubs;
    private String thumbImg;
    private Long discountRate;

    public ProductDto(Category categoryId, boolean isOwn, String name, Long price, boolean isSubs, String thumbImg, Long discountRate) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
    }

}
