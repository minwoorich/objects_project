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
public class ProductImage extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "product_image_id")
    private Long id;

    // TODO
    private Long productId;
    // TODO
    private Long imageId;

    @Builder
    private ProductImage(Long productId, Long imageId) {
        this.productId = productId;
        this.imageId = imageId;
    }
}
