package com.objects.marketbridge.domain.member.service;

import com.objects.marketbridge.domain.member.dto.SignUpDto;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.global.security.jwt.JwtToken;
import com.objects.marketbridge.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean isDuplicateEmail(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        return member != null;
    }

    @Transactional
    public void save(SignUpDto signUpDto) throws BadRequestException {
        boolean isDuplicateEmail = isDuplicateEmail(signUpDto.getEmail());

        if (isDuplicateEmail) throw new BadRequestException("이미 존재하는 이메일 입니다.");

        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member member = signUpDto.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    @Transactional
    public JwtToken signIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 리프레시 토큰 저장하는 로직 있어야 됨.
        String refreshToken = jwtToken.getRefreshToken();

        return jwtToken;
    }
}
