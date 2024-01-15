package com.objects.marketbridge.domain.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderTemp {
    @Column(name="order_temp_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String orderNo;

    Long amount;

    @Builder
    public OrderTemp(String orderNo, Long amount) {
        this.orderNo = orderNo;
        this.amount = amount;
    }
}
