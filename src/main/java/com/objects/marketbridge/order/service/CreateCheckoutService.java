package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.exception.exceptions.ErrorCode;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.SHIPPING_ADDRESS_NOT_REGISTERED;

@Service
@RequiredArgsConstructor
public class CreateCheckoutService {

    private final MemberRepository memberRepository;

    public CreateCheckoutHttp.Response create(Long memberId) {

        Member member = memberRepository.findByIdWithAddresses(memberId);
        Address address = filterDefaultAddress(member.getAddresses());

        return CreateCheckoutHttp.Response.of(address);
    }

    private Address filterDefaultAddress(List<Address> addresses) {

        return addresses.stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() -> CustomLogicException.builder()
                    .message("기본배송지가 없습니다")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorCode(SHIPPING_ADDRESS_NOT_REGISTERED)
                    .timestamp(LocalDateTime.now())
                    .build());
    }
}
