package com.objects.marketbridge.member.dto;


import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.product.domain.ProdOption;
import com.objects.marketbridge.product.domain.ProdTag;
import com.objects.marketbridge.product.domain.ProductImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistRequest {

    private Long productId;

    private Category category;

    private List<OrderDetail> orderDetails;

    private List<ProdOption> prodOptions;

    private List<ProductImage> productImages;

    private List<ProdTag> prodTags;

    private Boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Long price;

    private Boolean isSubs;

    private Long stock;

    private String thumbImg;

    private Long discountRate;

    private String productNo;
    @Builder
    public WishlistRequest(Long productId, Category category, List<OrderDetail> orderDetails, List<ProdOption> prodOptions, List<ProductImage> productImages, List<ProdTag> prodTags, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate, String productNo) {
        this.productId = productId;
        this.category = category;
        this.orderDetails = orderDetails;
        this.prodOptions = prodOptions;
        this.productImages = productImages;
        this.prodTags = prodTags;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.stock = stock;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.productNo = productNo;
    }
}

