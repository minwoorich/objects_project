package com.objects.marketbridge.domain.member.controller;


import com.objects.marketbridge.domain.member.dto.SignUpDto;
import com.objects.marketbridge.domain.member.dto.SignInDto;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.security.annotation.AuthMemberId;
import com.objects.marketbridge.global.security.jwt.JwtToken;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkDuplicateEmail(@RequestParam(name="email") String email) {
        return ApiResponse.ok(memberService.isDuplicateEmail(email));
    }

    @PostMapping("/sign-up")
    public ApiResponse<Void> registerUser(@Valid @RequestBody SignUpDto signUpDto) throws BadRequestException {
        memberService.save(signUpDto);
        return ApiResponse.create();
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtToken> signIn(@Valid @RequestBody SignInDto signInDto) {
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
