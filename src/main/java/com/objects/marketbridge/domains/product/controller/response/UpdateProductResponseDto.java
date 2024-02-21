package com.objects.marketbridge.domains.product.controller.response;

import com.objects.marketbridge.domains.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateProductResponseDto {

    private Long productId;

    private Long categoryId;
    private Boolean isOwn; // 로켓 true , 오픈 마켓 false
    private String name;
    private Long price;
    private Boolean isSubs;
    private Long stock;
    private String thumbImg;

//    private List<String> itemImgUrls = new ArrayList<>();
//    private List<String> detailImgUrls = new ArrayList<>();

    private Long discountRate;

//    private List<String> optionNames = new ArrayList<>();

    private String productNo;


    public UpdateProductResponseDto(Long productId) {
        this.productId = productId;
    }

    public UpdateProductResponseDto
            (Long categoryId, Boolean isOwn, String name, Long price,
             Boolean isSubs, Long stock, String thumbImg,
             Long discountRate, String productNo) {
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

    public UpdateProductResponseDto(Long productId, Long categoryId, Boolean isOwn,
                                    String name, Long price, Boolean isSubs, Long stock,
                                    String thumbImg, Long discountRate, String productNo) {
        this.productId = productId;
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

    public static UpdateProductResponseDto from(Product product){
        UpdateProductResponseDto updateProductResponseDto =
                new UpdateProductResponseDto(
                        product.getId(),
                        product.getCategory().getId(),
                        product.getIsOwn(),
                        product.getName(),
                        product.getPrice(),
                        product.getIsSubs(),
                        product.getStock(),
                        product.getThumbImg(),
                        product.getDiscountRate(),
                        product.getProductNo()
                );
        return updateProductResponseDto;
    }
}
