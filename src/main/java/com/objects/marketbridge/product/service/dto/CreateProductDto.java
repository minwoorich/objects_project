package com.objects.marketbridge.product.service.dto;

import com.objects.marketbridge.common.domain.Category;
import com.objects.marketbridge.product.controller.request.ProductCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateProductDto {

    private Long categoryId;
    private Boolean isOwn; // 로켓 true , 오픈 마켓 false
    private String name;
    private Long price;
    private Boolean isSubs;
    private Long stock;
    private String thumbImg;
    private Long discountRate;
    private String productNo;

    @Builder
    public CreateProductDto(Long categoryId, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate, String productNo) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.stock = stock;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.productNo = productNo;
    }

    public static CreateProductDto fromRequest(ProductCreateRequestDto request){
        return CreateProductDto.builder()
                .categoryId(request.getCategoryId())
                .isOwn(request.getIsOwn())
                .name(request.getName())
                .price(request.getPrice())
                .isSubs(request.getIsSubs())
                .stock(request.getStock())
                .thumbImg(request.getThumbImg())
                .discountRate(request.getDiscountRate())
                .productNo(request.getProductNo())
                .build();
    }

}
