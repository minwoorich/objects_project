package com.objects.marketbridge.member.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.member.dto.*;
import com.objects.marketbridge.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/address")
    public ApiResponse<List<GetAddressesResponse>> findAddrress(@AuthMemberId Long memberId){
        List<GetAddressesResponse> addressesResponses =memberService.findByMemberId(memberId);
        return ApiResponse.ok(addressesResponses);
    }

    @PostMapping("/address")
    public ApiResponse<List<GetAddressesResponse>> addAddressValue(
            @AuthMemberId Long memberId,
            @Valid @RequestBody AddAddressRequestDto request){
        List<GetAddressesResponse> addressesResponses = memberService.addMemberAddress(memberId,request);
       return ApiResponse.ok(addressesResponses);
    }

    @PatchMapping("/address/{addressId}")
    public ApiResponse<List<GetAddressesResponse>> updateAddress(
            @AuthMemberId Long memberId ,@Valid @RequestBody AddAddressRequestDto request ,
            @PathVariable (name = "addressId") Long addressId){
        List<GetAddressesResponse> addressesResponses = memberService.updateMemberAddress(memberId,addressId,request);
        return ApiResponse.ok(addressesResponses);
    }

    @DeleteMapping("/address/{addressId}")
    public ApiResponse<List<GetAddressesResponse>> deleteAddress(
            @AuthMemberId Long memberId ,@PathVariable (name = "addressId")  Long addressId){
        List<GetAddressesResponse> addressesResponses = memberService.deleteMemberAddress(memberId,addressId);
        return ApiResponse.ok(addressesResponses);
    }

    @GetMapping("/check-email")
    public ApiResponse<CheckedResultDto> checkDuplicateEmail(@RequestParam(name="email") String email) {
        CheckedResultDto checkedResultDto = memberService.isDuplicateEmail(email);
        return ApiResponse.ok(checkedResultDto);
    }

    //Wishlist 시작
    @GetMapping("/wishlist")
    public ApiResponse<Slice<WishlistResponse>> findWishlistByMemberId(
            @AuthMemberId Long memberId,
            @PageableDefault(value = 5, sort = {"createdAt"}, direction = Sort.Direction.DESC)Pageable pageable){
        Slice<WishlistResponse> wishlistResponse= memberService.findWishlistById(pageable,memberId);
        return ApiResponse.ok(wishlistResponse);
    }

    @PostMapping("/wishlist")
    public ApiResponse<String> addWish(
            @AuthMemberId Long memberId ,
            @RequestBody WishlistRequest request){
        memberService.addWish(memberId,request);
        return ApiResponse.create();
    }


}

