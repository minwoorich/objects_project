package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.infra.dtio.OrderDetailDtio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class OrderDetailInfo{
    private String orderNo;
    private Long orderDetailId;
    private Long productId;
    private Long quantity;
    private Long price;
    private String statusCode;
    private String deliveredDate;
    private String productThumbImageUrl;
    private String productName;
    private Boolean isOwn;

    @Builder
    private OrderDetailInfo(String orderNo, Long orderDetailId, Long productId, Long quantity, Long price, String statusCode, String deliveredDate, String productThumbImageUrl, String productName, Boolean isOwn) {
        this.orderNo = orderNo;
        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.statusCode = statusCode;
        this.deliveredDate = deliveredDate;
        this.productThumbImageUrl = productThumbImageUrl;
        this.productName = productName;
        this.isOwn = isOwn;
    }

    public static OrderDetailInfo of(OrderDetailDtio orderDetailDtio) {
        return OrderDetailInfo.builder()
                .orderNo(orderDetailDtio.getOrderNo())
                .orderDetailId(orderDetailDtio.getOrderDetailId())
                .productId(orderDetailDtio.getProduct().getProductId())
                .quantity(orderDetailDtio.getQuantity())
                .price(orderDetailDtio.getPrice())
                .statusCode(orderDetailDtio.getStatusCode())
                .deliveredDate(orderDetailDtio.getDeliveredDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .productThumbImageUrl(orderDetailDtio.getProduct().getThumbImg())
                .productName(orderDetailDtio.getProduct().getName())
                .isOwn(orderDetailDtio.getProduct().getIsOwn())
                .build();
    }

    public static OrderDetailInfo create(String orderNo, Long orderDetailId, Long productId, Long quantity, Long price, String statusCode, String deliveredDate, String productThumbImageUrl, String productName, Boolean isOwn) {
        return OrderDetailInfo.builder()
                .orderNo(orderNo)
                .orderDetailId(orderDetailId)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .statusCode(statusCode)
                .deliveredDate(deliveredDate)
                .productThumbImageUrl(productThumbImageUrl)
                .productName(productName)
                .isOwn(isOwn)
                .build();
    }
}
