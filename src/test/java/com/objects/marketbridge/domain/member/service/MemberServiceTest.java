package com.objects.marketbridge.domain.member.service;


import com.objects.marketbridge.domain.member.dto.FindPointDto;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Membership;
import com.objects.marketbridge.domain.model.Point;
import com.objects.marketbridge.domain.point.repository.PointRepository;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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

    @Autowired
    PointRepository pointRepository;

    Member originMember;

    @BeforeEach
    public void createMemberOrigin() {
        // 생성된 사용자 객체를 저장할 변수
        originMember=memberRepository.save(createMember("test1234@gmail.com"));
    }

    @AfterEach
    public void rollback(){
        memberRepository.deleteAll();
        pointRepository.deleteAllInBatch();
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
        memberService.changeMemberShip(originMember.getId());
        //then
        assertThat(originMember.getMembership()).isEqualTo(memberShipData);
    }


    @Test
    @DisplayName("포인트 조회 API")
    public void testFindPointById(){
            //given
        Member member = memberRepository.findByEmail("test1234@gmail.com").orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
        Point point = createPoint(member, 4500L);
        pointRepository.save(point);

            //when
        FindPointDto findMember = memberService.findPointById(member.getId());


            //then
        assertThat(findMember.getBalance()).isEqualTo(4500L);

    }

    private Member createMember(String email) {
        return Member.builder()
                .email(email)
                .membership(Membership.BASIC)
                .build();
    }

    private Point createPoint(Member member, Long balance) {
        Point point = Point.builder().balance(balance).member(member).build();
        point.setMember(member);

        return point;
    }

}