package com.objects.marketbridge.domain.member.service;


import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Membership;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private Member existingMember;

    @BeforeEach
    public void createMemberOrigin() {
        // 생성된 사용자 객체를 저장할 변수
        existingMember = Member.builder().email("test1234@gmail.com").membership(Membership.BASIC).build();
        memberRepository.save(existingMember);
    }

    @Test
    @DisplayName("이메일은 중복되지 않은 경우.")
    public void checkEmail() throws Exception{
        //given
        String emailCheck = "code11@gmail.com";

        //when
        Boolean isEmailDuplicate = memberService.checkEmail(emailCheck);

        //then
        assertThat(isEmailDuplicate).isFalse(); // 중복되지 않는 경우 true 반환을 확인
    }

    @Test
    @DisplayName("이메일이 중복되는 경우")
    public void testCheckEmailForDuplicate() {
        // given
        String emailToCheck = "test1234@gmail.com";

        //when
        Boolean isEmailDuplicate = memberService.checkEmail(emailToCheck);

        //then
        assertThat(isEmailDuplicate).isTrue(); // 중복되지 않는 경우 true 반환을 확인
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


}