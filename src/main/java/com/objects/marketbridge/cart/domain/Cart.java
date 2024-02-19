package com.objects.marketbridge.cart.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Boolean isSubs;


    private Long quantity;

    @Builder
    private Cart(Member member, Product product, Boolean isSubs, Long quantity) {
        this.member = member;
        this.product = product;
        this.isSubs = isSubs;
        this.quantity = quantity;
    }

    public static Cart create(Member member, Product product, Boolean isSubs, Long quantity) {
        // 1. 재고 검증 로직
        product.verifyStockAvailable(quantity);

        return Cart.builder()
                .member(member)
                .product(product)
                .isSubs(isSubs)
                .quantity(quantity)
                .build();
    }
}