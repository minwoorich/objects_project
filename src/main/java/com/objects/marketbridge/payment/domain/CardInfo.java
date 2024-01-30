package com.objects.marketbridge.payment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class CardInfo {

    @JsonProperty("card_corp_name")
    private String cardIssuerName;

    private String cardPurchaseName;

    @JsonProperty("install_month")
    private String cardInstallMonth;

    @Builder
    public CardInfo(String cardIssuerName, String cardPurchaseName, String cardInstallMonth) {
        this.cardIssuerName = cardIssuerName;
        this.cardPurchaseName = cardPurchaseName;
        this.cardInstallMonth = cardInstallMonth;
    }
}
