package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.dto.SignUpDto;
import com.objects.marketbridge.member.service.port.AuthRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto) throws BadRequestException {

        boolean isDuplicateEmail = memberService.isDuplicateEmail(signUpDto.getEmail()).isChecked();

        if (isDuplicateEmail) throw new BadRequestException("이미 존재하는 이메일 입니다.");

        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member member = signUpDto.toEntity(encodedPassword);

        memberService.save(member);
    }
}
