package com.objects.marketbridge.member.service;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.dto.*;
import com.objects.marketbridge.member.service.port.MemberRepository;

import com.objects.marketbridge.order.service.port.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.INVALID_PASSWORD;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.MEMBER_NOT_FOUND;


@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }


    public MemberEmail getEmail(Long memberId) {
        return memberRepository.getEmailById(memberId);
    }

    public GetMemberInfo getMemberInfo(Long memberId, String password) {
        try {
            return memberRepository.getMemberInfoByIdAndPassword(memberId, password);
        } catch (JpaObjectRetrievalFailureException e) {
            log.error(e.getMessage(), e);
            throw CustomLogicException.createBadRequestError(INVALID_PASSWORD);
        }
    }

    @Transactional
    public void updateMemberInfo(Long memberId, UpdateMemberInfo updateMemberInfo) {
        Member member = memberRepository.findById(memberId);
        String encodedPassword = passwordEncoder.encode(updateMemberInfo.password());
        member.updateMemberInfo(
                updateMemberInfo.email(),
                updateMemberInfo.name(),
                encodedPassword,
                updateMemberInfo.phoneNo(),
                updateMemberInfo.isAlert(),
                updateMemberInfo.isAgree()
        );
    }

    public MemberEmail findMemberEmail(String name, String phoneNo) {
        try {
            return memberRepository.findEmailByNameAndPhoneNo(name, phoneNo);
        } catch (JpaObjectRetrievalFailureException e) {
            log.error(e.getMessage(), e);
            throw CustomLogicException.createBadRequestError(MEMBER_NOT_FOUND);
        }
    }

    public MemberId findMemberId(String name, String email) {
        try {
            return memberRepository.findIdByNameAndEmail(name, email);
        } catch (JpaObjectRetrievalFailureException e) {
            log.error(e.getMessage(), e);
            throw CustomLogicException.createBadRequestError(MEMBER_NOT_FOUND);
        }
    }

    @Transactional
    public void updatePassword(UpdatePassword updatePassword) {
        Member member = memberRepository.findById(updatePassword.memberId());
        String encodedPassword = passwordEncoder.encode(updatePassword.password());
        member.updatePassword(encodedPassword);
    }
}
