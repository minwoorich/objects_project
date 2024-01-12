package com.objects.marketbridge.domain.order.domain;

import com.objects.marketbridge.domain.model.BaseEntity;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdOrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_order_id")
    private ProdOrder prodOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private Integer usedCoupon;

    private Integer quantity;

    private Integer price;

    private String statusCode;

    private LocalDateTime deliveredDate;

    private Integer usedPoint;

    private String reason;

    private LocalDateTime cancelledAt;

    @Builder
    private ProdOrderDetail(ProdOrder prodOrder, Product product, Coupon coupon, Integer usedCoupon, Integer quantity, Integer price, String statusCode, LocalDateTime deliveredDate, Integer usedPoint, String reason, LocalDateTime cancelledAt) {
        this.prodOrder = prodOrder;
        this.product = product;
        this.coupon = coupon;
        this.usedCoupon = usedCoupon;
        this.quantity = quantity;
        this.price = price;
        this.statusCode = statusCode;
        this.deliveredDate = deliveredDate;
        this.usedPoint = usedPoint;
        this.reason = reason;
        this.cancelledAt = cancelledAt;
    }

    public void setOrder(ProdOrder prodOrder) {
        this.prodOrder = prodOrder;
    }

    public void changeStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static ProdOrderDetail create(Product product, Coupon coupon, Integer quantity, Integer price) {
        return ProdOrderDetail.builder()
                .product(product)
                .coupon(coupon)
                .quantity(quantity)
                .price(price)
                .statusCode(StatusCodeType.ORDER_INIT.getCode())
                .build();
    }

    public Product cancel(String reason) {
        if (Objects.equals(statusCode, StatusCodeType.DELIVERY_COMPLETED.getCode())) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        statusCode = StatusCodeType.ORDER_CANCEL.getCode();
        this.reason = reason;

        return product;
    }
}
