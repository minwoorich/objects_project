package com.objects.marketbridge.domain.payment.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class PaymentCancel {
    private Long cancelAmount;
    private String paymentCancelReason;
    private String canceledAt;
    private String transactionKey;
}
