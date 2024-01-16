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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    private Member existingMember;

    @BeforeEach
    void init() {
        Member member = Member.builder()
                .email("iiwisii@naver.com")
                .name("박정인")
                .password("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4")
                .phoneNo("01073784758")
                .isAgree(true)
                .isAlert(true)
                .membership(Membership.WOW.toString())
                .socialType(SocialType.DEFAULT.toString())
                .build();

        memberRepository.save(member);
    }

    @BeforeEach
    public void createMemberOrigin() {
        // 생성된 사용자 객체를 저장할 변수
        existingMember = Member.builder().email("test1234@gmail.com").membership(Membership.BASIC).build();
        memberRepository.save(existingMember);
    }

    @Test
    @DisplayName("이메일이 중복이 되었으면 true를 반환한다")
    public void checkDuplicateEmailTrue() {
        //given
            String email = "iiwisii@naver.com";

        //when
        boolean isDuplicateEmail = memberService.isDuplicateEmail(email);

        //then
        assertThat(isDuplicateEmail).isTrue();
    }

    @Test
    @DisplayName("이메일이 중복이 되지 않았으면 false를 반환한다")
    public void checkDuplicateEmailFalse() {
        //given
        String email = "iiii@naver.com";

        //when
        boolean isDuplicateEmail = memberService.isDuplicateEmail(email);

        //then
        assertThat(isDuplicateEmail).isFalse();
    }

    @Test
    @Transactional
    @DisplayName("멤버십 변경 API")
    public void testUpdateWowMemberShip(){
        //given
        Membership memberShipData = Membership.WOW;
        //when
        memberService.changeMemberShip(existingMember.getId());
        //then
        assertThat(existingMember.getMembership()).isEqualTo(memberShipData);
    }

    //sign up 테스트
    //sign in 테스트
}