package com.objects.marketbridge.domain.member.service;

import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.dto.CreateMember;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.global.security.jwt.JwtToken;
import com.objects.marketbridge.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean checkDuplicateEmail(String email){

        Member memberEmail = memberRepository.findByEmail(email).orElse(null);

        if (memberEmail != null) {
            // null이 아니면 이미 가입된 email
            // 이미 등록된 아이디라는 문구 출력
            //....1
            return true;
        } else {
            // 사용할 수 있는 email
            // 정상적인 가입이 가능하다
            // ....0
            return false;
        }
    }

    public void save(CreateMember createMember){
        Member member = Member.fromDto(createMember);
        memberRepository.save(member);
    }

    @Transactional
    public JwtToken signIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
