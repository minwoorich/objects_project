package com.objects.marketbridge.domain.order.controller.response;

import com.objects.marketbridge.domain.order.domain.OrderDetail;
import com.objects.marketbridge.common.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponse {

    private Long productId;
    private String productNo;
    private String name;
    private Long price;
    private Long quantity;

    @Builder
    private ProductResponse(Long productId, String productNo, String name, Long price,Long quantity) {
        this.productId = productId;
        this.productNo = productNo;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static ProductResponse of(Product product, Long quantity) {
        return ProductResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(quantity)
                .build();
    }

    public static ProductResponse of(OrderDetail orderDetail) {
        return ProductResponse.builder()
                .productId(orderDetail.getProduct().getId())
                .productNo(orderDetail.getProduct().getProductNo())
                .name(orderDetail.getProduct().getName())
                .price(orderDetail.getProduct().getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
