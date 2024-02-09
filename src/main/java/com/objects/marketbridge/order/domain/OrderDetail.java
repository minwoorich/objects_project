package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_CANCELLABLE_PRODUCT;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.QUANTITY_EXCEEDED;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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

    private Long numberOfCancellations;

    @Builder
    private OrderDetail(Order order, String orderNo, String tid, Product product, MemberCoupon memberCoupon,  Long quantity, Long price, String statusCode, LocalDateTime deliveredDate, String reason, Long sellerId, LocalDateTime cancelledAt, Long numberOfCancellations) {
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
        this.numberOfCancellations = numberOfCancellations;
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

    public static OrderDetail create(OrderDetail orderDetail) {
        return OrderDetail.builder()
                .tid(orderDetail.getTid())
                .order(orderDetail.getOrder())
                .orderNo(orderDetail.getOrderNo())
                .product(orderDetail.getProduct())
                .memberCoupon(orderDetail.getMemberCoupon())
                .price(orderDetail.getPrice())
                .sellerId(orderDetail.getSellerId())
                .quantity(orderDetail.getNumberOfCancellations())
                .statusCode(ORDER_PARTIAL_CANCEL.getCode())
                .numberOfCancellations(0L)
                .build();
    }

    public void cancel(String reason, Long numberOfCancellations, DateTimeHolder dateTimeHolder) {
        // TODO 정책 정해야 함
        if (impossibleCancel())
            throw createNonCancellableProductError(dateTimeHolder.getTimeNow());
        if (exceededQuantity(numberOfCancellations))
            throw createQuantityExceededError(dateTimeHolder.getTimeNow());

        this.product.increase(numberOfCancellations);
        this.numberOfCancellations += numberOfCancellations;
        this.reason = reason;
        this.statusCode = ORDER_CANCEL.getCode();

        returnMemberCoupon();
    }

    public Integer totalAmount(Long quantity, LocalDateTime dateTime) {
        if(exceededQuantity(quantity))
            throw createQuantityExceededError(dateTime);

        return (int) (price * quantity);
    }

    public Integer totalAmount() {
        return totalAmount(this.quantity, LocalDateTime.now());
    }

    public Integer totalAmount(Long quantity) {
       return totalAmount(quantity, LocalDateTime.now());
    }

    public Integer cancelAmount() {
        return (int) (numberOfCancellations * price);
    }

    public void changeMemberCouponInfo(DateTimeHolder dateTimeHolder) {
        if (hasMemberCoupon())
            memberCoupon.changeUsageInfo(dateTimeHolder);
    }

    public void returnMemberCoupon() {
        changeMemberCouponInfo(null);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private CustomLogicException createQuantityExceededError(LocalDateTime dateTime) {
        return CustomLogicException.builder()
                .errorCode(QUANTITY_EXCEEDED)
                .timestamp(dateTime)
                .message("수량이 초과 되었습니다.")
                .httpStatus(BAD_REQUEST)
                .build();
    }

    private CustomLogicException createNonCancellableProductError(LocalDateTime dateTime) {
        return CustomLogicException.builder()
                .httpStatus(BAD_REQUEST)
                .message("취소가 불가능한 상품입니다.")
                .timestamp(dateTime)
                .errorCode(NON_CANCELLABLE_PRODUCT)
                .build();
    }

    private boolean exceededQuantity(Long numberOfCancellations) {
        return this.numberOfCancellations + numberOfCancellations > this.quantity;
    }

    private boolean impossibleCancel() {
        return Objects.equals(this.statusCode, DELIVERY_COMPLETED.getCode())
                || Objects.equals(this.statusCode, ORDER_PARTIAL_CANCEL.getCode());
    }

    private boolean hasMemberCoupon() {
        return memberCoupon != null;
    }
}
