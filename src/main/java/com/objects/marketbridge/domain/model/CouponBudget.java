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
public class CouponBudget extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "estimated_time_id")
    private Long id;
    // TODO
    private Long userId;

    private int price;

    @Builder
    private CouponBudget(Long userId, int price) {
        this.userId = userId;
        this.price = price;
    }
}
