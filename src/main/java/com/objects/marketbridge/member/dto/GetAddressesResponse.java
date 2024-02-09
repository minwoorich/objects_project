package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.AddressValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAddressesResponse {

    private AddressValue addressValue;

    private Boolean isDefault;

    @Builder
    public GetAddressesResponse(AddressValue addressValue, Boolean isDefault) {
        this.addressValue = addressValue;
        this.isDefault = isDefault;
    }

    public static GetAddressesResponse of(Address address){
        return GetAddressesResponse.builder()
                .addressValue(address.getAddressValue())
                .isDefault(address.getIsDefault()).build();
    }
}
