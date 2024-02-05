package com.objects.marketbridge.product.service.dto;

import com.objects.marketbridge.product.domain.ProductImage;
import com.objects.marketbridge.product.controller.request.UpdateProductRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@NoArgsConstructor
@Getter
@Component
public class UpdateProductDto {

    private Long categoryId;
    private Boolean isOwn; // 로켓 true , 오픈 마켓 false
    private String name;
    private Long price;
    private Boolean isSubs;
    private Long stock;
    private String thumbImg;
    private Long discountRate;
    private List<ProductImage> productImages;
    private String productNo;

    @Builder
    public UpdateProductDto(Long categoryId, Boolean isOwn, String name, Long price,
                            Boolean isSubs, Long stock, String thumbImg, Long discountRate,
                            List<ProductImage> productImages, String productNo) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.stock = stock;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.productImages = productImages;
        this.productNo = productNo;
    }

    public static UpdateProductDto fromRequest(UpdateProductRequestDto request){
        return UpdateProductDto.builder()
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
