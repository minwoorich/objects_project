package com.objects.marketbridge.domain.order.domain;

import com.objects.marketbridge.domain.model.BaseEntity;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.ProdOption;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.objects.marketbridge.domain.order.domain.StatusCodeType.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_option_id")
    private ProdOption prodOption;

    private Long usedCoupon;

    private Long quantity;

    private Long price;

    private String statusCode;

    private LocalDateTime deliveredDate;

    private Long usedPoint;

    private String reason;

    private LocalDateTime cancelledAt;

    @Builder
    private ProdOrderDetail(ProdOrder prodOrder, Product product, Coupon coupon, Long usedCoupon, Long quantity, Long price, String statusCode, LocalDateTime deliveredDate, Long usedPoint, String reason, LocalDateTime cancelledAt) {
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

    public static ProdOrderDetail create(Product product, Coupon coupon, Long quantity, Long price, String statusCode) {
        return ProdOrderDetail.builder()
                .product(product)
                .coupon(coupon)
                .quantity(quantity)
                .price(price)
                .statusCode(statusCode)
                .build();
    }

    public void cancel(String reason, String statusCode) {
        if (Objects.equals(statusCode, DELIVERY_COMPLETED.getCode())) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
