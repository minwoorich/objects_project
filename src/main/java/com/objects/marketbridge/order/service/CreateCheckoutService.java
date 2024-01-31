package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.exception.error.CustomLogicException;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttpDto;
import com.objects.marketbridge.order.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.objects.marketbridge.common.exception.error.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@Service
@RequiredArgsConstructor
public class CreateCheckoutService {

    private final MemberRepository memberRepository;

    public CreateCheckoutHttpDto.Response create(Long memberId) {

        Member member = memberRepository.findByIdWithAddresses(memberId);
        Address address = filterDefaultAddress(member.getAddresses());

        return CreateCheckoutHttpDto.Response.of(address);
    }

    private Address filterDefaultAddress(List<Address> addresses) {

        return addresses.stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() -> new CustomLogicException(SHIPPING_ADDRESS_NOT_REGISTERED.getMessage(), SHIPPING_ADDRESS_NOT_REGISTERED));
    }
}
