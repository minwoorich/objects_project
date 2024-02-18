package com.objects.marketbridge.member.controller;

import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.GetAuthentication;
import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.common.security.dto.JwtTokenDto;
import com.objects.marketbridge.common.security.service.JwtTokenProvider;
import com.objects.marketbridge.member.dto.SignUpDto;
import com.objects.marketbridge.member.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.objects.marketbridge.member.constant.MemberConst.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        return ApiResponse.create();
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtTokenDto> signIn(@GetAuthentication CustomUserDetails principal) {
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(principal);
        return ApiResponse.ok(jwtTokenDto);
    }

    @DeleteMapping("/sign-out")
    public ApiResponse<Void> signOut(@AuthMemberId Long memberId) {
        jwtTokenProvider.deleteToken(memberId);
        return ApiResponse.of(HttpStatus.OK, SIGNED_OUT_SUCCESSFULLY, null);
    }

    @PutMapping("/re-issue")
    public ApiResponse<JwtTokenDto> reIssueToken(@GetAuthentication CustomUserDetails principal) {
        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(principal);
        return ApiResponse.ok(jwtTokenDto);
    }
}
