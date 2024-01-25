package com.objects.marketbridge.domain.payment.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Amount {

    private Long total;
    private Long discount;

    @Builder
    public Amount(Long total, Long discount) {
        this.total = total;
        this.discount = discount;
    }
}
