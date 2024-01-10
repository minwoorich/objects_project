package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProdOption extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "prod_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_category_id")
    private OptionCategory optionCategory;

    private String name;

    private Integer price;

    @Builder
    private ProdOption(Product product, OptionCategory optionCategory, String name, Integer price) {
        this.product = product;
        this.optionCategory = optionCategory;
        this.name = name;
        this.price = price;
    }
}
