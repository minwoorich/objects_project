package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancelPayment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
