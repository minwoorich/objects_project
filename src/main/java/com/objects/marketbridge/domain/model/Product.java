package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryId;

    private boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Long price;

    private boolean isSubs;

    private String thumbImg;

    private Long discountRate;

    private Long stock;

    @Builder
    public Product(Category categoryId, boolean isOwn, String name, Long price, boolean isSubs, String thumbImg, Long discountRate, Long stock) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.stock = stock;
    }
}
