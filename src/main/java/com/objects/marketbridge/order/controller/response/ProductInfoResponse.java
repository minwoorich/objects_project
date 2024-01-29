package com.objects.marketbridge.order.controller.response;

import com.objects.marketbridge.order.service.dto.ProductInfoResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductInfoResponse {
    
    private Long quantity;
    private String name;
    private Long price;
    private String image; // TODO 주문 취소 요청 이미지 반환

    @Builder
    private ProductInfoResponse(Long quantity, String name, Long price, String image) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public static ProductInfoResponse of(ProductInfoResponseDto serviceDto) {
        return ProductInfoResponse.builder()
                .quantity(serviceDto.getQuantity())
                .name(serviceDto.getName())
                .price(serviceDto.getPrice())
                .image(serviceDto.getImage())
                .build();
    }

}
