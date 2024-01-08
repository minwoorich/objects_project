package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    // TODO
    private Long userId;

    @Embedded
    private AddressValue addressValue;

    private boolean idDefault;

    @Builder
    private Address(Long userId, AddressValue addressValue, boolean idDefault) {
        this.userId = userId;
        this.addressValue = addressValue;
        this.idDefault = idDefault;
    }

}
