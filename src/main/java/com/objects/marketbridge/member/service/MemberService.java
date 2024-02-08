package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.service.port.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public CheckedResultDto isDuplicateEmail(String email){
        boolean isDuplicateEmail = memberRepository.existsByEmail(email);
        return CheckedResultDto.builder().checked(isDuplicateEmail).build();
    }

    public void addMemberAddress(Long id){
        memberRepository.findByIdWithAddresses(id);
    }


    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

}
