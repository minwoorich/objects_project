package com.objects.marketbridge.domain.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Transfer {
    private String bankCode;
    private String settlementStatus;

    @Builder
    public Transfer(String bankCode, String settlementStatus) {
        this.bankCode = bankCode;
        this.settlementStatus = settlementStatus;
    }
}
