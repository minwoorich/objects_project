package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.AddressValue;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddAddressRequestDto {

    private Long addressId;

    private AddressValue addressValue;

    private Boolean isDefault;

    @Builder
    public AddAddressRequestDto( Long addressId,AddressValue addressValue,Boolean isDefault) {
        this.addressId = addressId;
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
