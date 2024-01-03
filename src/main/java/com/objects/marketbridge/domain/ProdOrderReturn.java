package com.objects.marketbridge.domain;

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
public class ProdOrderReturn extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "prod_order_return_id")
    private Long id;

    // TODO
    private Long orderId;

    private String statusCode; // 00

    private String reason;

    @Builder
    private ProdOrderReturn(Long orderId, String statusCode, String reason) {
        this.orderId = orderId;
        this.statusCode = statusCode;
        this.reason = reason;
    }
}
