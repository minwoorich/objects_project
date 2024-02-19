package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.dto.*;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.WishRepository;
import com.objects.marketbridge.order.service.port.AddressRepository;
import com.objects.marketbridge.product.domain.Product;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.objects.marketbridge.member.dto.WishlistResponse.of;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final WishRepository wishRepository;

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

    public void addWish(Long memberId, WishlistRequest request){
        Member member = memberRepository.findById(memberId);
        Product product = Product.builder()
                .category(request.getCategory())
                .isOwn(request.getIsOwn())
                .name(request.getName())
                .price(request.getPrice())
                .isSubs(request.getIsSubs())
                .stock(request.getStock())
                .thumbImg(request.getThumbImg())
                .discountRate(request.getDiscountRate())
                .productNo(request.getProductNo())
                .build();

        Wishlist wishlist = Wishlist.builder()
                .member(member)
                .product(product).build();

        wishRepository.save(wishlist);
    }

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

//    private Boolean checkWishlist(Long id){
//        WishlistResponse wishlistResponse = of(wishRepository.findById(id));
//        if(wishlistResponse.getWishlists())
//    }

}
