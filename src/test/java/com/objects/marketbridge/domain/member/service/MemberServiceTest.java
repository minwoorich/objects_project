package com.objects.marketbridge.domain.member.service;


import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Membership;
import com.objects.marketbridge.domain.model.SocialType;
import com.objects.marketbridge.global.security.jwt.JwtToken;
import com.objects.marketbridge.global.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;
    private Member existingMember; // 생성된 사용자 객체를 저장할 변수


    @BeforeEach
    public void createMemberOrigin() {
        existingMember = Member.builder().email("pmmh9395@gmail.com").build();
        memberRepository.save(existingMember);
    }

    @Test
    @DisplayName("이메일은 중복되지 않은 경우.")
    public void checkEmail() throws Exception{
        //given
        String emailCheck = "code11@gmail.com";

        //when
        Boolean isEmailDuplicate = memberService.checkDuplicateEmail(emailCheck);

        //then
        assertThat(isEmailDuplicate).isFalse(); // 중복되지 않는 경우 false 반환을 확인
    }

    @Test
    @DisplayName("이메일이 중복되는 경우")
    public void testCheckEmailForDuplicate() {
        // given
        String emailToCheck = "pmmh9395@gmail.com";

        // when
        Boolean isEmailDuplicate = memberService.checkDuplicateEmail(emailToCheck);

        // then
        assertThat(isEmailDuplicate).isTrue(); // 중복되는 경우 true 반환을 확인
    }


    @Test
    public void getTokenTest() {

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        //given
        Member member = Member.builder()
                .email("iiwisii@naver.com")
                .name("박정인")
                .password("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4")
                .phoneNo("01073784758")
                .isAgree(true)
                .isAlert(true)
                .membership(Membership.WOW.toString())
                .socialType(SocialType.DEFAULT.toString())
                .roles(roles)
                .build();

        memberRepository.save(member);
        JwtToken jwt = memberService.signIn(member.getEmail(), member.getPassword());

        //when


        //then
    }
}