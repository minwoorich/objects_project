package com.objects.marketbridge.product.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.domain.ImageType;
import com.objects.marketbridge.product.domain.ProductImage;
import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
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
    private List<ProductImageDto> productImageList;
    private List<OptionDto> optionInfoList;

    @Builder
    public CreateProductDto(Long categoryId, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate, String productNo,List<ProductImageDto> productImageList,List<OptionDto> optionInfoList) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.stock = stock;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.productNo = productNo;
        this.productImageList = productImageList;
        this.optionInfoList = optionInfoList;
    }

    public static CreateProductDto fromRequest(CreateProductRequestDto request){
        List<ProductImageDto> productImageList= toProductImageDto(request.getItemImgUrls(), ImageType.ITEM_IMG.toString());
        productImageList.addAll(toProductImageDto(request.getDetailImgUrls(), ImageType.DETAIL_IMG.toString()));

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
                .productImageList(productImageList)
                .optionInfoList(toOptionDto(request.getOptionInfo()))
                .build();
    }

    public static List<OptionDto> toOptionDto(Map<String,String> optionInfo){
        List<OptionDto> optionDtos = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(optionInfo);

        Iterator<String> keys = jsonObject.keySet().iterator();
        while (keys.hasNext()){
            String k = keys.next();
            optionDtos.add(new OptionDto().create(k,jsonObject.getString(k)));
        }

        return optionDtos;
    }

    public static List<ProductImageDto> toProductImageDto(List<String> imgUrls,String type){
        List<ProductImageDto> productImageDtos = new ArrayList<>();
        for (int i = 0; i < imgUrls.size(); i++) {
            productImageDtos.add(new ProductImageDto().create(imgUrls.get(i),type,Long.valueOf(i)));
        }
        return productImageDtos;
    }
}
