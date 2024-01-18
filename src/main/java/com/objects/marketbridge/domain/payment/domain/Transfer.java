package com.objects.marketbridge.domain.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Transfer {
    private String bankCode;

    @Builder
    public Transfer(String bankCode) {
        this.bankCode = bankCode;
    }
}
