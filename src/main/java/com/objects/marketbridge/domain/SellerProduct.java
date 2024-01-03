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
public class SellerProduct extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "seller_product_id")
    private Long id;
    // TODO
    private Long userId;
    // TODO
    private Long productId;

    @Builder
    private SellerProduct(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
