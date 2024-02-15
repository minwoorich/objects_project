package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.dto.AddAddressRequestDto;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.GetAddressesResponse;
import com.objects.marketbridge.member.service.port.MemberRepository;

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

    public List<GetAddressesResponse> addMemberAddress(Long id , AddAddressRequestDto addAddressRequestDto){
        Member member = memberRepository.findById(id);
        member.addAddress(addAddressRequestDto.toEntity());
        memberRepository.save(member);
        return member.getAddresses().stream().map(GetAddressesResponse::of).collect(Collectors.toList());
    }

    public List<GetAddressesResponse> updateMemberAddress(Long memberId,Long addressId,AddAddressRequestDto request){
        Member member = memberRepository.findById(memberId);
        Address address = addressRepository.findById(addressId);
        address.update(request.getAddressValue());
        return member.getAddresses().stream().map(GetAddressesResponse::of).collect(Collectors.toList());
    }

    public List<GetAddressesResponse> deleteMemberAddress(Long memberId,Long addressId){
        Member member = memberRepository.findById(memberId);
        addressRepository.deleteById(addressId);
        return member.getAddresses().stream().map(GetAddressesResponse::of).collect(Collectors.toList());
    }

//    public List<Wishlist> addWishlist (Long memberId , Long productId){
//
//
//        return
//    }

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

}
