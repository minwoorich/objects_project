package com.objects.marketbridge.domain.payment.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoPayApproveRequest {

    private String cid;

    private String tid;

    @JsonProperty("partner_order_id")
    private String partnerOrderId;

    @JsonProperty("partner_user_id")
    private String partnerUserId;

    @JsonProperty("pg_token")
    private String pgToken;

    @JsonProperty("total_amount")
    private String totalAmount;

    @Builder
    public KakaoPayApproveRequest(String cid, String tid, String partnerOrderId, String partnerUserId, String pgToken, String totalAmount) {
        this.cid = cid;
        this.tid = tid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.pgToken = pgToken;
        this.totalAmount = totalAmount;
    }
}
