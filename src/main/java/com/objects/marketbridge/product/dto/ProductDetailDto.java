package com.objects.marketbridge.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class ProductDetailDto {

    // 상품 테이블 정보 가져오기
    private Long productId;
    private Long price;
    private Long discoutRate;
    private String name;
    private String thumUrl;
    private Boolean isOwn;
    private Boolean isSubs;
    // 상품 태그 정보 가져오기
    private List<ProdTagDto> tagInfos;
    // 상품 옵션 정보 가져오기
    private List<OptionDto> optionInfos;
    // 상품 이미지 정보 가져오기
    private List<ProductImageDto> imageDtos;
    // 옵션만 다른 상품 가져오기 (상품 번호 일치)
    private List<ProductSimpleDto> productSimpleDtos;
    // 카테고리 정보 가져오기
    private String categoryInfo;
    // 셀러 정보 가져오기 - TODO
    // 찜 리스트 정보 가져오기 - TODO


    @Builder
    private ProductDetailDto(Long productId, Long price, Long discoutRate, String name, String thumUrl, Boolean isOwn, Boolean isSubs, String categoryInfo) {
        this.productId = productId;
        this.price = price;
        this.discoutRate = discoutRate;
        this.name = name;
        this.thumUrl = thumUrl;
        this.isOwn = isOwn;
        this.isSubs = isSubs;
        this.tagInfos = new ArrayList<>();
        this.optionInfos = new ArrayList<>();
        this.imageDtos = new ArrayList<>();
        this.productSimpleDtos = new ArrayList<>();
        this.categoryInfo = categoryInfo;
    }

    public ProductDetailDto create(Long productId, Long price, Long discoutRate, String name, String thumUrl, Boolean isOwn, Boolean isSubs, String categoryInfo) {
        return ProductDetailDto.builder()
                .productId(productId)
                .price(price)
                .discoutRate(discoutRate)
                .name(name)
                .thumUrl(thumUrl)
                .isOwn(isOwn)
                .isSubs(isSubs)
                .categoryInfo(categoryInfo)
                .build();
    }

    public void addAllTagInfo(List<ProdTagDto> prodTagDto){
        this.tagInfos.addAll(prodTagDto);
    }

    public void addAllOptionInfo(List<OptionDto> optionDto){
        this.optionInfos.addAll(optionDto);
    }

    public void addAllImageDto(List<ProductImageDto> productImageDto){
        this.imageDtos.addAll(productImageDto);
    }

    public void addAllProductSimpleDto(List<ProductSimpleDto> productSimpleDto){
        this.productSimpleDtos.addAll(productSimpleDto);
    }


}
