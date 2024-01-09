package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;
    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponId")
    private Coupon couponId;

    private boolean isUsed;

    private LocalDateTime usedDate;

    @Builder
    private CustomerCoupon(User userId, Coupon couponId, boolean isUsed, LocalDateTime usedDate) {
        this.userId = userId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.usedDate = usedDate;
    }
}
