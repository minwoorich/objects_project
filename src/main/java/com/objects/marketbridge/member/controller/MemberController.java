package com.objects.marketbridge.member.controller;

import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.member.service.MemberService;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.GetAuthentication;
import com.objects.marketbridge.common.security.dto.JwtTokenDto;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.member.constant.MemberConst;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.SignInDto;
import com.objects.marketbridge.member.dto.SignUpDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public ApiResponse<CheckedResultDto> checkDuplicateEmail(@RequestParam(name="email") String email) {
        CheckedResultDto checked = memberService.isDuplicateEmail(email);
        return ApiResponse.ok(checked);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto signUpDto) throws BadRequestException {
        memberService.save(signUpDto);
        return ApiResponse.create();
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtTokenDto> signIn(@Valid @RequestBody SignInDto signInDto) {
        JwtTokenDto jwtTokenDto = memberService.signIn(signInDto);
        return ApiResponse.ok(jwtTokenDto);
    }

    @DeleteMapping("/sign-out")
    public ApiResponse<Void> signOut(@AuthMemberId Long memberId) {
        memberService.signOut(memberId);
        return ApiResponse.of(HttpStatus.OK, MemberConst.LOGGED_OUT_SUCCESSFULLY, null);
    }

    @PutMapping("/re-issue")
    public ApiResponse<JwtTokenDto> reIssueToken(@GetAuthentication CustomUserDetails principal) {
        JwtTokenDto jwtTokenDto = memberService.reIssueToken(principal);
        return ApiResponse.ok(jwtTokenDto);
    }

    @GetMapping("/membership/{id}")
    public void changeMembership(@PathVariable Long id){
        memberService.changeMemberShip(id);
    }

//    @GetMapping("/point/{id}")
//    public ApiResponse<FindPointDto> findPointById(@PathVariable Long id){
//
//        FindPointDto memberPoint = memberService.findPointById(id);
//        return ApiResponse.of(HttpStatus.OK, memberPoint);
//    }
}
