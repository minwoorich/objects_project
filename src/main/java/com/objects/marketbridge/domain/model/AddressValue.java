package com.objects.marketbridge.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressValue {

    private String phoneNo;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    private String alias;

    @Builder
    private AddressValue(String phoneNo, String name, String city, String street, String zipcode, String alias) {
        this.phoneNo = phoneNo;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.alias = alias;
    }
}
