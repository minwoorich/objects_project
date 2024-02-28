package com.objects.marketbridge.domains.member.service;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.domains.member.domain.Address;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.domain.Wishlist;
import com.objects.marketbridge.domains.member.dto.*;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import com.objects.marketbridge.domains.member.service.port.WishRepository;
import com.objects.marketbridge.domains.order.service.port.AddressRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final WishRepository wishRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

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

    public Slice<WishlistResponse> findWishlistById(Pageable pageable, Long memberId){
        Slice<Wishlist> wishlists = wishRepository.findByMemberId(pageable,memberId);
        List<WishlistResponse> responses = wishlists.getContent()
                .stream().map(WishlistResponse::of).collect(Collectors.toList());

        return new SliceImpl<>(responses,pageable,wishlists.hasNext());
    }

    public Boolean checkWishlist(Long memberId, WishlistRequest request){
        //true면 wishList에 이미 존재 false면 wishList 추가가능
        Long wishResult = wishRepository.countByProductIdAndMemberId(memberId, request.getProductId());
        return wishResult == 1L;
    }

    @Transactional
    public void addWish(Long memberId, WishlistRequest request){
        Member member = memberRepository.findById(memberId);
        Product product = productRepository.findById(request.getProductId());

        Wishlist wishlist = Wishlist.create(product , member);

        wishRepository.save(wishlist);

    }

    @Transactional
    public void deleteWishlist(Long memberId,WishlistRequest request) {
        wishRepository.deleteWishlist(memberId,request.getProductId());
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
            throw CustomLogicException.createBadRequestError(MEMBER_NOT_FOUND);
        }
    }

    public MemberId findMemberId(String name, String email) {
        try {
            return memberRepository.findIdByNameAndEmail(name, email);
        } catch (JpaObjectRetrievalFailureException e) {
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
