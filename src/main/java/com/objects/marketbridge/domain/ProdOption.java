package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    // TODO
    private Long productId;
    // TODO
    private Long optionCategoryId;

    private String name;

    private Integer price;

    @Builder
    private ProdOption(Long productId, Long optionCategoryId, String name, Integer price) {
        this.productId = productId;
        this.optionCategoryId = optionCategoryId;
        this.name = name;
        this.price = price;
    }
}
