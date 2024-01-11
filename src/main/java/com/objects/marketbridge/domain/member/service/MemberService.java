package com.objects.marketbridge.domain.member.service;

import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.member.dto.CreateMember;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
}
