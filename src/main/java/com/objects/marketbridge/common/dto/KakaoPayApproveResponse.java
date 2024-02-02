package com.objects.marketbridge.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("item_name")
    private String orderName;

    private Long quantity;

    private Amount amount;

    @JsonProperty("card_info")
    private CardInfo cardInfo;

    @JsonProperty("approved_at")
    private LocalDateTime approvedAt;

    @Builder
    public KakaoPayApproveResponse(String aid, String tid, String cid, String sid, String partnerOrderId, String partnerUserId, String paymentMethodType, String orderName, Long quantity, Amount amount, CardInfo cardInfo, LocalDateTime approvedAt, String pgToken) {
        this.aid = aid;
        this.tid = tid;
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.quantity = quantity;
        this.amount = amount;
        this.cardInfo = cardInfo;
        this.orderName = orderName;
        this.approvedAt = approvedAt;
    }
}
