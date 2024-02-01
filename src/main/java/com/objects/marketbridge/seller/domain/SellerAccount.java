package com.objects.marketbridge.seller.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_account_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    Seller seller;

    Long balance;
    Long incoming;
    Long outgoing;

    @Builder
    public SellerAccount(Long balance, Long incoming, Long outgoing, Seller seller) {
        this.seller = seller;
        this.balance = balance;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    // 연관관계 편의 메서드
    public void linkSeller(Seller seller) {
        this.seller = seller;
    }
}
