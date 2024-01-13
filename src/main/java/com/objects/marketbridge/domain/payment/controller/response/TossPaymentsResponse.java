package com.objects.marketbridge.domain.payment.controller.response;

import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.PaymentCancel;
import com.objects.marketbridge.domain.payment.domain.VirtualAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TossPaymentsResponse {

    private String receiptId;
    private String orderId; // [주의] : PK 가 아니라 주문번호임
    private String paymentType; // 일반결제, 브랜드페이
    private String paymentMethod; // CARD, TRANSFER, VIRTUAL
    private Long totalAmount;
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
    private PaymentCancel paymentCancel;

    // 카드 결제
    private Card card;

    //가상 계좌 결제
    private VirtualAccount virtual;
}
