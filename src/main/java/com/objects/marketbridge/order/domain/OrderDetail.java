package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.springframework.http.HttpStatus.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_coupon_id")
    private MemberCoupon memberCoupon;

    private Long quantity;

    private String orderNo;

    private Long price; // quantity * 상품 price

    private String statusCode;

    private LocalDateTime deliveredDate;

    private String reason;

    private String tid;

    private Long sellerId;

    private LocalDateTime cancelledAt;

    @Builder
    private OrderDetail(Order order, String orderNo, String tid, Product product, MemberCoupon memberCoupon,  Long quantity, Long price, String statusCode, LocalDateTime deliveredDate, String reason, Long sellerId, LocalDateTime cancelledAt) {
        this.orderNo = orderNo;
        this.tid = tid;
        this.order = order;
        this.product = product;
        this.memberCoupon = memberCoupon;
        this.quantity = quantity;
        this.price = price;
        this.statusCode = statusCode;
        this.deliveredDate = deliveredDate;
        this.reason = reason;
        this.sellerId = sellerId;
        this.cancelledAt = cancelledAt;
    }

    // 연관관계 메서드
    public void setOrder(Order order) {
        this.order = order;
    }

    // 비즈니스 로직
    public void changeStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static OrderDetail create(String tid, Order order, Product product, String orderNo, MemberCoupon memberCoupon, Long price, Long quantity, Long sellerId, String statusCode) {

        return OrderDetail.builder()
                .tid(tid)
                .order(order)
                .orderNo(orderNo)
                .product(product)
                .memberCoupon(memberCoupon)
                .quantity(quantity)
                .price(price)
                .sellerId(sellerId)
                .statusCode(statusCode)
                .build();
    }

    public Integer changeReasonAndStatus(String reason, String statusCode, Long numberOfCancellations) {
        // TODO 정책 정해야 함
        if (Objects.equals(this.statusCode, DELIVERY_COMPLETED.getCode())) {
            throw CustomLogicException.builder()
                    .httpStatus(BAD_REQUEST)
                    .message("취소가 불가능한 상품입니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(NON_CANCELLABLE_PRODUCT)
                    .build();
        }
        Integer totalAmount = totalAmount(numberOfCancellations, LocalDateTime.now());
        this.quantity -= numberOfCancellations;
        this.product.increase(numberOfCancellations);
        this.reason = reason;
        this.statusCode = statusCode;
        return totalAmount;
    }

    public Integer totalAmount(Long quantity, LocalDateTime dateTime) {
        if(quantity > this.quantity) {
            throw CustomLogicException.builder()
                    .errorCode(QUANTITY_EXCEEDED)
                    .timestamp(dateTime)
                    .message("수량이 초과 되었습니다.")
                    .httpStatus(BAD_REQUEST)
                    .build();
        }
        return (int) (price * quantity);
    }

    public Integer totalAmount() {
        return totalAmount(this.quantity, LocalDateTime.now());
    }

    public void changeMemberCouponInfo(DateTimeHolder dateTimeHolder) {
        if (memberCoupon != null) {
            memberCoupon.changeUsageInfo(dateTimeHolder);
        }
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
