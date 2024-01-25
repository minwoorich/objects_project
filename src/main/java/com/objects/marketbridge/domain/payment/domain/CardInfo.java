package com.objects.marketbridge.domain.payment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class CardInfo {

    @JsonProperty("kakaopay_purchase_corp")
    private String kakaopayPurchaseCorp;

    @JsonProperty("kakaopay_issuer_corp")
    private String kakaopayIssuerCorp;

    @JsonProperty("install_month")
    private String installMonth;

    @Builder
    public CardInfo(String kakaopayPurchaseCorp, String kakaopayIssuerCorp, String installMonth) {
        this.kakaopayPurchaseCorp = kakaopayPurchaseCorp;
        this.kakaopayIssuerCorp = kakaopayIssuerCorp;
        this.installMonth = installMonth;
    }
}
