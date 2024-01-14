package com.objects.marketbridge.domain.member.service;

import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.dto.CreateMemberDto;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

        public boolean checkEmail(String email){  //코드를 간단하게 리팩토링
        return memberRepository.findByEmail(email).isPresent(); // 반환값 true :이메일중복 false: 사용가능이메일
    }

        public void createMember(CreateMemberDto createMemberDto){
        Member member = Member.fromDto(createMemberDto);
        memberRepository.save(member);
    }

    @Transactional
    public void changeMemberShip(Long id){
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id)); // id 를 통한 멤버조회

        if(findMember.getMembership().equals(Membership.BASIC)){//멤버십 WOW 등록
            findMember.setMembership(Membership.WOW);
            memberRepository.save(findMember);
        }else {// 멤버십 BASIC으로 해제
            findMember.setMembership(Membership.BASIC);
            memberRepository.save(findMember);
        }


    }
}
