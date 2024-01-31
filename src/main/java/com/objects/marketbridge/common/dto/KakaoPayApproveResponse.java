package com.objects.marketbridge.common.dto;

import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class KakaoPayApproveResponse {

    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private String orderName;
    private LocalDateTime approvedAt;

    private Amount amount;
    private CardInfo cardInfo;

    @Builder
    public KakaoPayApproveResponse(String aid, String tid, String cid, String sid, String partnerOrderId, String partnerUserId, String paymentMethodType, Amount amount, CardInfo cardInfo, String orderName, LocalDateTime approvedAt) {
        this.aid = aid;
        this.tid = tid;
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.amount = amount;
        this.cardInfo = cardInfo;
        this.orderName = orderName;
        this.approvedAt = approvedAt;
    }
}
