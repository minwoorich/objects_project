package com.objects.marketbridge.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelPayment extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "cancel_payment_id")
    private Long id;
    // TODO
    private Long orderId;

    // TODO 정인님 과제
    private String code;

    @Builder
    private CancelPayment(Long orderId, String code) {
        this.orderId = orderId;
        this.code = code;
    }
}
