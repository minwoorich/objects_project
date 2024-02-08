package com.objects.marketbridge.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductSimpleDto {
    private Long productId;
    private String thumUrl;
    private String name;
    private Long discountRate;
    private Boolean isOwn;
    private Boolean isStock;
    private Long price;
    private String categoryLevel;

    @Builder
    @QueryProjection
    public ProductSimpleDto(Long productId, String thumUrl, String name, Long discountRate, Boolean isOwn, Long price,String categoryLevel) {
        this.productId = productId;
        this.thumUrl = thumUrl;
        this.name = name;
        this.discountRate = discountRate;
        this.isOwn = isOwn;
        this.price = price;
        this.categoryLevel = categoryLevel;
    }

    public void addStockInfo(Boolean isStock){
        this.isStock = isStock;
    }
}
