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
public class MemberCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_id")
    private Long id;

    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;
    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponId")
    private Coupon couponId;

    private boolean isUsed;

    private LocalDateTime usedDate;

    @Builder
    private MemberCoupon(Member memberId, Coupon couponId, boolean isUsed, LocalDateTime usedDate) {
        this.memberId = memberId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.usedDate = usedDate;
    }
}
