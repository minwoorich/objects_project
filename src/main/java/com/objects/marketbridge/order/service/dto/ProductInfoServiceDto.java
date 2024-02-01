package com.objects.marketbridge.order.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductInfoServiceDto {

    private Long quantity;
    private String name;
    private Long price;
    private String image; // TODO 주문 취소 요청 이미지 반환

    @Builder
    private ProductInfoServiceDto(Long quantity, String name, Long price, String image) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
