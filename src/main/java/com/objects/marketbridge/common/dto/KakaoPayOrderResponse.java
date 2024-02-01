package com.objects.marketbridge.common.dto;

import com.objects.marketbridge.payment.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPayOrderResponse {
    private String tid;
    private String cid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private CanceledAmount canceledAmount;
    private CanceledAvailableAmount canceledAvailableAmount;
    private String itemName;
    private String quantity;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;
    private CardInfo selectedCardInfo;
    private List<PaymentActionDetail> paymentActionDetails;

    @Builder
    public KakaoPayOrderResponse(String tid, String cid, String status, String partnerOrderId, String partnerUserId, String paymentMethodType, Amount amount, CanceledAmount canceledAmount, CanceledAvailableAmount canceledAvailableAmount, String itemName, String quantity, LocalDateTime approvedAt, LocalDateTime canceledAt, CardInfo selectedCardInfo, List<PaymentActionDetail> paymentActionDetails) {
        this.tid = tid;
        this.cid = cid;
        this.status = status;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.amount = amount;
        this.canceledAmount = canceledAmount;
        this.canceledAvailableAmount = canceledAvailableAmount;
        this.itemName = itemName;
        this.quantity = quantity;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.selectedCardInfo = selectedCardInfo;
        this.paymentActionDetails = paymentActionDetails;
    }
}
