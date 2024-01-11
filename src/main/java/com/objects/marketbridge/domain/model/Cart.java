package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_option_id")
    private ProdOption prodOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    private boolean isSubs;

    private Integer quantity;

    @Builder
    private Cart(ProdOption prodOptionId, Member memberId, boolean isSubs, Integer quantity) {
        this.prodOptionId = prodOptionId;
        this.memberId = memberId;
        this.isSubs = isSubs;
        this.quantity = quantity;
    }

    //TODO : 컬럼 채우기 및 엔티티 완성하기
}