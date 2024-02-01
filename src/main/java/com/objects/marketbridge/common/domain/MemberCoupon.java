package com.objects.marketbridge.common.domain;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
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
    private Member member;
    // TODO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private Boolean isUsed;

    private LocalDateTime usedDate;

    private LocalDateTime endDate;

    @Builder
    private MemberCoupon(Member member, Coupon coupon, Boolean isUsed, LocalDateTime usedDate, LocalDateTime endDate) {
        this.member = member;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.usedDate = usedDate;
        this.endDate = endDate;
    }

    public void changeUsageInfo(DateTimeHolder dateTimeHolder) {
        isUsed = !isUsed;
        usedDate = dateTimeHolder == null ? null : dateTimeHolder.getTimeNow();
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void applyCouponUsage(Boolean isUsed, DateTimeHolder dateTimeHolder) {
        this.isUsed = isUsed;
        this.usedDate = dateTimeHolder.getTimeNow();
    }
}
