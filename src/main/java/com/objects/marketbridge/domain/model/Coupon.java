package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "coupon")
    private List<MemberCoupon> memberCoupons = new ArrayList<>();

    private String name;

    private Long price;

    private Long count;

    private Long minimumPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    private Coupon(String name, Long price, Product product, Long count, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.price = price;
        this.product = product;
        this.count = count;
        this.minimumPrice = minimumPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addMemberCoupon(MemberCoupon memberCoupon) {
        memberCoupons.add(memberCoupon);
        memberCoupon.setCoupon(this);
    }

    public void returnCoupon() {
        memberCoupons.forEach(MemberCoupon::returnCoupon);
        this.count += 1;
    }
}
