package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponBudget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_budget_id")
    private Long id;
    // TODO
    private Long memberId;

    private int price;

    @Builder
    private CouponBudget(Long memberId, int price) {
        this.memberId = memberId;
        this.price = price;
    }
}
