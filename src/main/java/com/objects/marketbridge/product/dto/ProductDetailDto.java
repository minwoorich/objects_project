package com.objects.marketbridge.product.dto;

import com.objects.marketbridge.review.dto.ReviewWholeInfoDto;
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
    private List<ProdTagDto> tagInfos = new ArrayList<>();
    // 상품 옵션 정보 가져오기
    private List<OptionDto> optionInfos = new ArrayList<>();
    // 상품 이미지 정보 가져오기
    private List<ProductImageDto> imageDtos = new ArrayList<>();
    // 옵션만 다른 상품 가져오기 (상품 번호 일치)
    private List<ProductSimpleDto> productSimpleDtos = new ArrayList<>();
    // 리뷰 정보 가져오기
    // 카테고리 정보 가져오기
    private String categoryInfo;
    // 셀러 정보 가져오기 - TODO
    // 찜 리스트 정보 가져오기 - TODO

    @Builder
    private ProductDetailDto(Long productId, Long price, Long discoutRate, String name, String thumUrl, Boolean isOwn, Boolean isSubs, List<ProdTagDto> tagInfos, List<OptionDto> optionInfos, List<ProductImageDto> imageDtos, List<ProductSimpleDto> productSimpleDtos, String categoryInfo) {
        this.productId = productId;
        this.price = price;
        this.discoutRate = discoutRate;
        this.name = name;
        this.thumUrl = thumUrl;
        this.isOwn = isOwn;
        this.isSubs = isSubs;
        this.tagInfos = tagInfos;
        this.optionInfos = optionInfos;
        this.imageDtos = imageDtos;
        this.productSimpleDtos = productSimpleDtos;
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

    public void addTagInfo(ProdTagDto prodTagDto){
        this.tagInfos.add(prodTagDto);
    }

    public void addOptionInfo(OptionDto optionDto){
        this.optionInfos.add(optionDto);
    }

    public void addImageDto(ProductImageDto productImageDto){
        this.imageDtos.add(productImageDto);
    }

    public void addProductSimpleDto(ProductSimpleDto productSimpleDto){
        this.productSimpleDtos.add(productSimpleDto);
    }


}
