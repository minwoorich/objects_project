package com.objects.marketbridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Warehouse extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "warehouse_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller sellerId;

    private String name;

    private String alias;

    @Embedded
    private AddressValue addressValue;

    @Builder
    public Warehouse(Seller sellerId, String name, String alias, AddressValue addressValue) {
        this.sellerId = sellerId;
        this.name = name;
        this.alias = alias;
        this.addressValue = addressValue;
    }
}
