package com.objects.marketbridge.common.security.service;

import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.member.dto.AuthMember;
import com.objects.marketbridge.member.service.port.AuthRepository;
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

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AuthMember member = authRepository.findAuthMemberByEmail(email);
        // db에 저장하지 않고 여기서 권한 부여
        return CustomUserDetails.builder()
                .id(member.id())
                .email(member.email())
                .password(member.password())
                .roles(List.of("USER"))
                .build();
    }
}
