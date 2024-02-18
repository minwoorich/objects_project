package com.objects.marketbridge.cart.service.dto;

import com.objects.marketbridge.cart.domain.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GetCartDto {
    private Long productId;
    private String productNo;
    private String productName;
    private Long productPrice;
    private Long quantity;
    private Long discountRate;
    private String thumbImageUrl;
    private Boolean isOwn;
    private Boolean isSubs;
    private Long stock;
    private Long deliveryFee;
    private String deliveredDate; // yyyy.MM.dd
    private List<String> optionNames;

    @Builder
    private GetCartDto(Long productId, String productNo, String productName, Long productPrice, Long quantity, Long discountRate, String thumbImageUrl, Boolean isOwn, Boolean isSubs, Long stock, Long deliveryFee, String deliveredDate, List<String> optionNames) {
        this.productId = productId;
        this.productNo = productNo;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.discountRate = discountRate;
        this.thumbImageUrl = thumbImageUrl;
        this.isOwn = isOwn;
        this.isSubs = isSubs;
        this.stock = stock;
        this.deliveryFee = deliveryFee;
        this.deliveredDate = deliveredDate;
        this.optionNames = optionNames;
    }

    public static GetCartDto of(Cart cart) {
        return GetCartDto.builder()
                .productId(cart.getProduct().getId())
                .productNo(cart.getProduct().getProductNo())
                .productName(cart.getProduct().getName())
                .productPrice(cart.getProduct().getPrice())
                .quantity(cart.getQuantity())
                .discountRate(cart.getProduct().getDiscountRate())
                .thumbImageUrl(cart.getProduct().getThumbImg())
                .isOwn(cart.getProduct().getIsOwn())
                .isSubs(cart.getProduct().getIsSubs())
                .stock(cart.getProduct().getStock())
                .deliveryFee(0L)
                .deliveredDate("deliveredDate")
                .optionNames(cart.getProduct().getProdOptions().stream().map(po -> po.getOption().getName()).collect(Collectors.toList()))
                .build();
    }

    public static GetCartDto create(Long productId, String productNo, String productName, Long productPrice, Long quantity, Long discountRate, String thumbImageUrl, Boolean isOwn, Boolean isSubs, Long stock, Long deliveryFee, String deliveredDate, List<String> optionNames) {
        return GetCartDto.builder()
                .productId(productId)
                .productNo(productNo)
                .productName(productName)
                .productPrice(productPrice)
                .quantity(quantity)
                .stock(stock)
                .discountRate(discountRate)
                .thumbImageUrl(thumbImageUrl)
                .isOwn(isOwn)
                .isSubs(isSubs)
                .deliveryFee(deliveryFee)
                .deliveredDate(deliveredDate)
                .optionNames(optionNames)
                .build();
    }
}
