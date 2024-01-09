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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Embedded
    private AddressValue addressValue;

    private boolean idDefault;

    @Builder
    private Address(User userId, AddressValue addressValue, boolean idDefault) {
        this.userId = userId;
        this.addressValue = addressValue;
        this.idDefault = idDefault;
    }

}
