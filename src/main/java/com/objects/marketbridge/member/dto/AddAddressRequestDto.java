package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.order.domain.Address;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddAddressRequestDto {

    private Address address;

    @Builder
    public AddAddressRequestDto(Address address) {
        this.address = address;
    }

    public Address toEntity(AddAddressRequestDto addAddressRequestDto) {
        return Address.builder()
                .addressValue(addAddressRequestDto.getAddress().getAddressValue())
                .isDefault(addAddressRequestDto.getAddress().getIsDefault())
                .build();
    }
}
