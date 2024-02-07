package com.objects.marketbridge.order.infra.dtio;

import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderDetailDtio {

    private Long orderDetailId;
    private ProductDto product;
    private Long quantity;
    private String orderNo;
    private Long price;
    private String statusCode;
    private LocalDateTime deliveredDate;
    private Long sellerId;
    private LocalDateTime cancelledAt;

    @Builder
    public OrderDetailDtio(Long orderDetailId, ProductDto product, Long quantity, String orderNo, Long price, String statusCode, LocalDateTime deliveredDate, Long sellerId, LocalDateTime cancelledAt) {
        this.orderDetailId = orderDetailId;
        this.product = product;
        this.quantity = quantity;
        this.orderNo = orderNo;
        this.price = price;
        this.statusCode = statusCode;
        this.deliveredDate = deliveredDate;
        this.sellerId = sellerId;
        this.cancelledAt = cancelledAt;
    }

    public static OrderDetailDtio of(OrderDetail orderDetail) {
        return OrderDetailDtio.builder()
                .orderDetailId(orderDetail.getId())
                .quantity(orderDetail.getQuantity())
                .orderNo(orderDetail.getOrderNo())
                .price(orderDetail.getPrice())
                .statusCode(orderDetail.getStatusCode())
                .deliveredDate(orderDetail.getDeliveredDate())
                .sellerId(orderDetail.getSellerId())
                .cancelledAt(orderDetail.getCancelledAt())
                .product(ProductDto.of(orderDetail.getProduct()))
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class ProductDto{

        private Long productId;
        private String optionName;
        private Boolean isOwn; // 로켓 true , 오픈 마켓 false
        private String name;
        private Long price;
        private String thumbImg;
        private String productNo;

        @Builder
        private ProductDto(Long productId, String optionName, Boolean isOwn, String name, Long price, String thumbImg, String productNo) {
            this.productId = productId;
            this.optionName = optionName;
            this.isOwn = isOwn;
            this.name = name;
            this.price = price;
            this.thumbImg = thumbImg;
            this.productNo = productNo;
        }

        public static ProductDto of(Product product) {
            return ProductDto.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .isOwn(product.getIsOwn())
                    .price(product.getPrice())
                    .thumbImg(product.getThumbImg())
                    .productNo(product.getProductNo())
                    .build();

        }
    }
}
