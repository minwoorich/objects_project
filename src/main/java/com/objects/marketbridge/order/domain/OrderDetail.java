package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_CANCELLABLE_PRODUCT;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.QUANTITY_EXCEEDED;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE order_detail SET deleted_at = now() WHERE order_detail_id = ?")
@SQLRestriction("deleted_at is NULL")
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

    private Long price;

    private String statusCode;

    private LocalDateTime deliveredDate;

    private String tid;

    private Long sellerId;

    private LocalDateTime cancelledAt;

    private Long reducedQuantity;

    @Builder
    private OrderDetail(Order order, String orderNo, String tid, Product product, MemberCoupon memberCoupon,  Long quantity, Long price, String statusCode, LocalDateTime deliveredDate, Long sellerId, LocalDateTime cancelledAt, Long reducedQuantity) {
        this.orderNo = orderNo;
        this.tid = tid;
        this.order = order;
        this.product = product;
        this.memberCoupon = memberCoupon;
        this.quantity = quantity;
        this.price = price;
        this.statusCode = statusCode;
        this.deliveredDate = deliveredDate;
        this.sellerId = sellerId;
        this.cancelledAt = cancelledAt;
        this.reducedQuantity = reducedQuantity;
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
                .orderNo(orderNo)
                .tid(tid)
                .order(order)
                .product(product)
                .memberCoupon(memberCoupon)
                .quantity(quantity)
                .price(price)
                .sellerId(sellerId)
                .statusCode(statusCode)
                .build();
    }

    public static OrderDetail create(OrderDetail orderDetail, String statusCode) {
        return OrderDetail.builder()
                .orderNo(orderDetail.getOrderNo())
                .tid(orderDetail.getTid())
                .order(orderDetail.getOrder())
                .product(orderDetail.getProduct())
                .memberCoupon(orderDetail.getMemberCoupon())
                .quantity(orderDetail.getReducedQuantity())
                .price(orderDetail.getPrice())
                .statusCode(statusCode)
                .deliveredDate(orderDetail.getDeliveredDate())
                .sellerId(orderDetail.getSellerId())
                .cancelledAt(orderDetail.getCancelledAt())
                .build();
    }

    public CancelReturnStatusCode cancel(Long numberOfCancellations, DateTimeHolder dateTimeHolder) {
        // TODO 정책 정해야 함
        if (impossibleCancel())
            throw CustomLogicException.createBadRequestError(NON_CANCELLABLE_PRODUCT, dateTimeHolder);

        return changeStatus(numberOfCancellations, dateTimeHolder, ORDER_CANCEL.getCode());
    }

    public CancelReturnStatusCode returns(Long numberOfReturns, DateTimeHolder dateTimeHolder) {
        if (impossibleReturn())
            throw CustomLogicException.createBadRequestError(NON_RETURNABLE_PRODUCT, dateTimeHolder);

        return changeStatus(numberOfReturns, dateTimeHolder, RETURN_INIT.getCode());
    }

    public Integer totalAmount(Long quantity, LocalDateTime dateTime) {
        if(exceededQuantity(quantity))
            throw CustomLogicException.createBadRequestError(QUANTITY_EXCEEDED, dateTime);

        return (int) (price * quantity);
    }

    public Integer totalAmount() {
        return totalAmount(this.quantity, LocalDateTime.now());
    }

    public Integer totalAmount(Long quantity) {
       return totalAmount(quantity, LocalDateTime.now());
    }

    public Integer cancelAmount() {
        return (int) (reducedQuantity * price);
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

    private CancelReturnStatusCode changeStatus(Long numberOfReduce, DateTimeHolder dateTimeHolder, String statusCode) {
        validateQuantity(numberOfReduce, dateTimeHolder);

        String previousStatusCode = updateProductAndStatus(numberOfReduce, statusCode);

        String cancelReturnStatusCode = determineReturnOrCancelStatus(statusCode);

        return new CancelReturnStatusCode(previousStatusCode, cancelReturnStatusCode);
    }

    private void validateQuantity(Long numberOfReduce, DateTimeHolder dateTimeHolder) {
        if (exceededQuantity(numberOfReduce)) {
            throw CustomLogicException.createBadRequestError(QUANTITY_EXCEEDED, dateTimeHolder);
        }
    }

    private String updateProductAndStatus(Long numberOfReduce, String statusCode) {
        String status = this.statusCode;

        this.product.increase(numberOfReduce);
        this.statusCode = statusCode;
        this.reducedQuantity += numberOfReduce;
        returnMemberCoupon();

        return status;
    }

    private String determineReturnOrCancelStatus(String statusCode) {
        boolean isCancel = statusCode.equals(ORDER_CANCEL.getCode());
        boolean isWhole = Objects.equals(quantity, reducedQuantity);

        if (isWhole) {
            return isCancel ? ORDER_CANCEL.getCode() : ORDER_RETURN.getCode();
        } else {
            return isCancel ? ORDER_PARTIAL_CANCEL.getCode() : ORDER_PARTIAL_RETURN.getCode();
        }
    }

    private boolean exceededQuantity(Long numberOfCancellations) {
        return this.reducedQuantity + numberOfCancellations > this.quantity;
    }

    private boolean impossibleCancel() {
        return Objects.equals(this.statusCode, DELIVERY_COMPLETED.getCode())
                || Objects.equals(this.statusCode, ORDER_PARTIAL_CANCEL.getCode());
    }

    private boolean hasMemberCoupon() {
        return memberCoupon != null;
    }

    private boolean impossibleReturn() {
        return !Objects.equals(DELIVERY_COMPLETED.getCode(), this.statusCode);
    }

    public void withdraw(Long withdrawQuantity, String previousStateCode) {
        valifyReducedQuantity(withdrawQuantity);
        product.decrease(withdrawQuantity);
        memberCoupon.changeUsageInfo(cancelledAt);
        statusCode = previousStateCode;
    }

    private void valifyReducedQuantity(Long quantity) {
        if (reducedQuantity - quantity < 0) {
            throw CustomLogicException.createBadRequestError(OUT_OF_WITHDRAW_QUANTITY);
        }
        reducedQuantity -= quantity;
    }
}
