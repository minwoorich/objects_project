package com.objects.marketbridge.domain.payment.domain;

import com.objects.marketbridge.domain.model.BaseEntity;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "prod_order_id")
    private ProdOrder prodOrder;

    private String receiptId;
    private String orderNo;
    private String paymentType; // 일반결제, 브랜드페이
    private String paymentMethod; // CARD, TRANSFER, VIRTUAL
    private Long totalPrice;
    private Long balanceAmount;
    private String requestedAt;
    private String approvedAt;
    private String paymentKey;
    private String settlementStatus; // 정산 상태
    private String paymentStatus;
    private String refundStatus;
    private String customerName;
    private String bankCode;
    private String orderName;
    private String phoneNo;

    // 결제 취소에 대한 값
    @Embedded
    private PaymentCancel paymentCancel;

    // 카드 결제
    @Embedded
    private Card card;

    //가상 계좌 결제
    @Embedded
    private VirtualAccount virtual;

    @Builder
    private Payment(ProdOrder prodOrder, String orderNo, String receiptId, String paymentType, String paymentMethod, Long totalPrice, Long balanceAmount, String requestedAt, String approvedAt, String paymentKey, String settlementStatus, String paymentStatus, String refundStatus, String customerName, String bankCode, String orderName, String phoneNo, PaymentCancel paymentCancel, Card card, VirtualAccount virtual) {
        this.prodOrder = prodOrder;
        this.orderNo = orderNo;
        this.receiptId = receiptId;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.balanceAmount = balanceAmount;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.paymentKey = paymentKey;
        this.settlementStatus = settlementStatus;
        this.paymentStatus = paymentStatus;
        this.refundStatus = refundStatus;
        this.customerName = customerName;
        this.bankCode = bankCode;
        this.orderName = orderName;
        this.phoneNo = phoneNo;
        this.paymentCancel = paymentCancel;
        this.card = card;
        this.virtual = virtual;
    }

    public static Payment create(String customerName, String orderName, Long totalPrice, String orderNo, String paymentKey, String phoneNo, String paymentStatus) {
        return Payment.builder()
                .customerName(customerName)
                .orderName(orderName)
                .totalPrice(totalPrice)
                .paymentKey(paymentKey)
                .orderNo(orderNo)
                .phoneNo(phoneNo)
                .paymentStatus(paymentStatus)
                .build();
    }
}
