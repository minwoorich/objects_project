package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.dto.AddAddressRequestDto;
import com.objects.marketbridge.member.dto.AddAddressResponseDto;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.GetAddressesResponse;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.Address;
import com.objects.marketbridge.order.service.port.AddressRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    public CheckedResultDto isDuplicateEmail(String email){
        boolean isDuplicateEmail = memberRepository.existsByEmail(email);
        return CheckedResultDto.builder().checked(isDuplicateEmail).build();
    }

    public List<GetAddressesResponse> findByMemberId(Long id){
        List<Address> addresses = addressRepository.findByMemberId(id);
        return addresses.stream().map(GetAddressesResponse::of).collect(Collectors.toList());
    }

    public AddAddressResponseDto addMemberAddress(Long id , AddAddressRequestDto addAddressRequestDto){
        Member member = memberRepository.findById(id);
        member.addAddress(addAddressRequestDto.toEntity(addAddressRequestDto));
        return AddAddressResponseDto.createDto(member.getAddresses());
    }

    public void updateMemberAddress(Long memberId,AddAddressRequestDto request){
        Address addressValueByAddressId = addressRepository.findAddressValueByAddressId(request.getAddress().getId(),memberId);
        addressValueByAddressId.update(request.getAddress().getAddressValue());
    }


    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

}
