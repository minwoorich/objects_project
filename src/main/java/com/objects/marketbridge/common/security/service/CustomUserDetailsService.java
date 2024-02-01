package com.objects.marketbridge.common.security.service;

import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.member.dto.AuthMember;
import com.objects.marketbridge.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AuthMember member = memberRepository.findAuthMemberByEmail(email);

        // db에 저장하지 않고 여기서 권한 부여
        List<String> roles = new ArrayList<>();
        roles.add("USER");

      return new CustomUserDetails(member.id(), member.email(), passwordEncoder.encode(member.password()), roles);
    }
}
