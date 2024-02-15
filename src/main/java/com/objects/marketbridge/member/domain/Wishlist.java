package com.objects.marketbridge.member.domain;

import com.objects.marketbridge.product.domain.ProdOption;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_option_id")
    private ProdOption productOption;

    @Builder
    public Wishlist(Member member, ProdOption productOption) {
        this.member = member;
        this.productOption = productOption;
    }

}
