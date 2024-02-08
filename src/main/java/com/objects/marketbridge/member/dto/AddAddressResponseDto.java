package com.objects.marketbridge.member.dto;

import com.objects.marketbridge.order.domain.Address;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddAddressResponseDto {

    private List<Address> addresses = new ArrayList<>();

    @Builder
    public AddAddressResponseDto(List<Address> addresses) {
        this.addresses = addresses;
    }
    public static AddAddressResponseDto createDto(List<Address> addresses){
        return AddAddressResponseDto.builder()
                .addresses(addresses)
                .build();
    }

}
