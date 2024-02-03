package com.objects.marketbridge.member.service;

import com.objects.marketbridge.common.domain.*;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPaySubsApproveResponse;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.CreateSubsDto;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
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
