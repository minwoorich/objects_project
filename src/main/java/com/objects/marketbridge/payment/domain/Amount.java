package com.objects.marketbridge.payment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Amount {

    private Long totalAmount;

    private Long discountAmount;

    @Builder
    public Amount(Long totalAmount, Long discountAmount) {
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
    }
}
