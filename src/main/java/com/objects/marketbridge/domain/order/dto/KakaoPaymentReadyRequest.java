package com.objects.marketbridge.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoPaymentReadyRequest {

    private String cid;

    @JsonProperty("partner_order_id")
    private String partnerOrderId;

    @JsonProperty("partner_user_id")
    private String partnerUserId;

    private Long quantity;

    @JsonProperty("total_amount")
    private Long totalAmount;

    @JsonProperty("tax_free_amount")
    private Long taxFreeAmount;

    @JsonProperty("approval_url")
    private String approvalUrl;

    @JsonProperty("cancel_url")
    private String cancelUrl;

    @JsonProperty("fail_url")
    private String failUrl;

    @Builder
    public KakaoPaymentReadyRequest(String cid, String partnerOrderId, String partnerUserId, Long quantity, Long totalAmount, Long taxFreeAmount, String approvalUrl, String cancelUrl, String failUrl) {
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.approvalUrl = approvalUrl;
        this.cancelUrl = cancelUrl;
        this.failUrl = failUrl;
    }
}
