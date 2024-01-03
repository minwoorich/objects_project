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

    // TODO
    private Long userId;

    private String alias;

    @Embedded
    private AddressValue addressValue;

    @Builder
    private Warehouse(Long userId, String alias, AddressValue addressValue) {
        this.userId = userId;
        this.alias = alias;
        this.addressValue = addressValue;
    }

}
