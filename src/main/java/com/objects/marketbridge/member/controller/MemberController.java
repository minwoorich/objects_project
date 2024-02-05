package com.objects.marketbridge.member.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.domain.AddressValue;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.common.dto.*;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.member.controller.request.CreateSubsRequest;
import com.objects.marketbridge.member.dto.*;
import com.objects.marketbridge.member.infra.MemberRepositoryImpl;
import com.objects.marketbridge.member.service.MemberService;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import com.objects.marketbridge.order.domain.Address;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

import static com.objects.marketbridge.common.config.KakaoPayConfig.SUBS_CID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/address")
    public ApiResponse<List<Address>> addAddressValue(
            @AuthMemberId Long memberId,
            @Valid @RequestBody Address request){
        Member member = memberRepository.findById(memberId);

        member.addAddress(request);

       return ApiResponse.ok(member.getAddresses());
    }

    @GetMapping("/check-email")
    public ApiResponse<CheckedResultDto> checkDuplicateEmail(@RequestParam(name="email") String email) {
        CheckedResultDto checkedResultDto = memberService.isDuplicateEmail(email);
        return ApiResponse.ok(checkedResultDto);
    }

}
