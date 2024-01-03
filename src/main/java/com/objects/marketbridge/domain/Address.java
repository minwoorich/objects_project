package com.objects.marketbridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    // 주소 별칭(집, 회사 등등)
    private String alias;
    @Embedded
    private AddressValue addressValue;

    private boolean idDefault;

    @Builder
    private Address(Long userId, String alias, AddressValue addressValue, boolean idDefault) {
        this.userId = userId;
        this.alias = alias;
        this.addressValue = addressValue;
        this.idDefault = idDefault;
    }

}
