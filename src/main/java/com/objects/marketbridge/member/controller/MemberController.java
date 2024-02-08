package com.objects.marketbridge.member.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.member.dto.AddAddressRequestDto;
import com.objects.marketbridge.member.dto.AddAddressResponseDto;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.GetAddressesResponse;
import com.objects.marketbridge.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/find-address")
    public ApiResponse<List<GetAddressesResponse>> findAddrress(@AuthMemberId Long memberId){
        List<GetAddressesResponse> addressesResponses =memberService.findByMemberId(memberId);
        return ApiResponse.ok(addressesResponses);
    }

    @PostMapping("/add-address")
    public ApiResponse<List<GetAddressesResponse>> addAddressValue(
            @AuthMemberId Long memberId,
            @Valid @RequestBody AddAddressRequestDto request){
        memberService.addMemberAddress(memberId,request);
        List<GetAddressesResponse> addressesResponses =memberService.findByMemberId(memberId);
       return ApiResponse.ok(addressesResponses);
    }

    @PatchMapping("/update-address")
    public ApiResponse<List<GetAddressesResponse>> updateAddress(
            @AuthMemberId Long memberId ,@Valid @RequestBody AddAddressRequestDto request){
//        memberService.updateMemberAddress(memberId,request);
        List<GetAddressesResponse> addressesResponses =memberService.findByMemberId(memberId);
        return ApiResponse.ok(addressesResponses);
    }

    @GetMapping("/check-email")
    public ApiResponse<CheckedResultDto> checkDuplicateEmail(@RequestParam(name="email") String email) {
        CheckedResultDto checkedResultDto = memberService.isDuplicateEmail(email);
        return ApiResponse.ok(checkedResultDto);
    }

}
