package com.objects.marketbridge.domain.order.dto;

import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.PaymentCancel;
import com.objects.marketbridge.domain.payment.domain.VirtualAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderDto {
    private Long usedCouponId;
    private Long unitOrderPrice;
    private Long memberId;
    private Long addressId;
    private String orderName;
    private String orderNo;
    private Long totalSavedPoint;
    private Long totalUsedCoupon;
    private Long membershipDiscountPrice;
    private Long totalOrderPrice;
    private Long realOrderPrice;
    private String paymentMethod; // CARD, TRANSFER, VIRTUAL
    private String paymentKey;
    private String bankCode;
    private PaymentCancel paymentCancel;
    private Card card;
    private VirtualAccount virtual;

    private List<ProductValue> productValues;

    @Builder
    public CreateOrderDto(Long usedCouponId, Long unitOrderPrice, Long memberId, Long addressId, String orderName, String orderNo, Long totalSavedPoint, Long totalUsedCoupon, Long membershipDiscountPrice, Long totalOrderPrice, Long realOrderPrice, String paymentMethod, String paymentKey, String bankCode, PaymentCancel paymentCancel, Card card, VirtualAccount virtual, List<ProductValue> productValues) {
        this.usedCouponId = usedCouponId;
        this.unitOrderPrice = unitOrderPrice;
        this.memberId = memberId;
        this.addressId = addressId;
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.totalSavedPoint = totalSavedPoint;
        this.totalUsedCoupon = totalUsedCoupon;
        this.membershipDiscountPrice = membershipDiscountPrice;
        this.totalOrderPrice = totalOrderPrice;
        this.realOrderPrice = realOrderPrice;
        this.paymentMethod = paymentMethod;
        this.paymentKey = paymentKey;
        this.bankCode = bankCode;
        this.paymentCancel = paymentCancel;
        this.card = card;
        this.virtual = virtual;
        this.productValues = productValues;
    }
}
