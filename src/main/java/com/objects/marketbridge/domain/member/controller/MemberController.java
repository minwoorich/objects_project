package com.objects.marketbridge.domain.member.controller;


import com.objects.marketbridge.domain.member.dto.CreateMember;
import com.objects.marketbridge.domain.member.dto.SignInDto;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import com.objects.marketbridge.global.security.jwt.JwtToken;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<Void> registerUser(@Valid @RequestBody CreateMember userDTO) {
        memberService.save(userDTO);
        return ApiResponse.of(HttpStatus.CREATED,"completed",null);
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        String email = signInDto.getEmail();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(email, password);

        return ApiResponse.ok(jwtToken);
    }

    @GetMapping("/test")
    public Long authTest(@AuthMemberId Long id) {
        log.info("id = {}", id);
        return id;
    }
}
