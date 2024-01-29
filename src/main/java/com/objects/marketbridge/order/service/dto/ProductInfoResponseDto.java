package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInfoResponseDto {

    private Long quantity;
    private String name;
    private Long price;
    private String image; // TODO 주문 취소 요청 이미지 반환

    @Builder
    private ProductInfoResponseDto(Long quantity, String name, Long price, String image) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public static ProductInfoResponseDto of(OrderDetail orderDetail) {
        return ProductInfoResponseDto.builder()
                .quantity(orderDetail.getQuantity())
                .name(orderDetail.getProduct().getName())
                .price(orderDetail.getProduct().getPrice())
                .image(orderDetail.getProduct().getThumbImg())
                .build();
    }
}
