package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerCoupon extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "customer_coupon_id")
    private Long id;

    // TODO
    private Long userId;
    // TODO
    private Long couponId;

    private boolean isUsed;

    private LocalDateTime usedDate;

    @Builder
    private CustomerCoupon(Long userId, Long couponId, boolean isUsed, LocalDateTime usedDate) {
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.usedDate = usedDate;
    }
}
