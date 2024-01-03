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
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "stock_id")
    private Long id;

    // TODO
    private Long productId;
    // TODO
    private Long warehouseId;
    // TODO
    private Long productOptionId;

    private Integer quantity;

    @Builder
    private Stock(Long productId, Long warehouseId, Long productOptionId, Integer quantity) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
    }
}
