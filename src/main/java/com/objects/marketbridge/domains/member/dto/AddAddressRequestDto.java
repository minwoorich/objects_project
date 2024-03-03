package com.objects.marketbridge.domains.member.dto;

import com.objects.marketbridge.domains.member.domain.Address;
import com.objects.marketbridge.domains.member.domain.AddressValue;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddAddressRequestDto {

    private AddressValue addressValue;

    private Boolean isDefault;

    @Builder
    public AddAddressRequestDto( AddressValue addressValue,Boolean isDefault) {
        this.addressValue = addressValue;
        this.isDefault=isDefault;
    }

    public Address toEntity() {
        return Address.builder()
                .addressValue(this.addressValue)
                .isDefault(this.isDefault)
                .build();
    }
}
