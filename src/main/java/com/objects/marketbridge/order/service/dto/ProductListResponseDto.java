package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductListResponseDto {

    private Long productId;
    private String productNo;
    private String name;
    private Long price;
    private Long quantity;

    @Builder
    private ProductListResponseDto(Long productId, String productNo, String name, Long price,Long quantity) {
        this.productId = productId;
        this.productNo = productNo;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static ProductListResponseDto of(Product product, Long quantity) {
        return ProductListResponseDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(quantity)
                .build();
    }

    public static ProductListResponseDto of(OrderDetail orderDetail) {
        return ProductListResponseDto.builder()
                .productId(orderDetail.getProduct().getId())
                .productNo(orderDetail.getProduct().getProductNo())
                .name(orderDetail.getProduct().getName())
                .price(orderDetail.getProduct().getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
