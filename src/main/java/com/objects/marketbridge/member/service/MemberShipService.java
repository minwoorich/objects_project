package com.objects.marketbridge.member.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.Membership;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.member.dto.CreateSubsDto;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import com.objects.marketbridge.payment.domain.Amount;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
public class MemberShipService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void changeMemberShip(Long id) {

        // 이제 orElseThrow 는 전부 MemberRepositoryImpl 에서 처리하기로 했습니다. by 정민우
//        Member findMember = memberRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id)); // id 를 통한 조회실패 예외발생

        Member findMember = memberRepository.findById(id);

        if (findMember.getMembership().equals("BASIC")) {//멤버십 WOW 등록
            findMember.setMembership(MembershipType.WOW.toString());
            memberRepository.save(findMember);
        } else {// 멤버십 BASIC으로 해제
            findMember.setMembership(MembershipType.BASIC.toString());
            memberRepository.save(findMember);
        }
    }

        @Transactional
        public void savePayReadyData(CreateSubsDto createSubsDto) { //CreateorderService

            // 1. sid 정보 저장
            Membership membership = membershipRepository.save(createSubscribtion(createSubsDto));

        }


    @Transactional
    public void saveAprrovalResponse(KakaoPayApproveResponse response){
        Membership membership = membershipRepository.findBySubsOrderNo(response.getPartnerOrderId());
        membership.update(response.getTid(),response.getSid(),response.getCid(),response.getPartnerOrderId(),response.getPartnerUserId(),response.getOrderName(),response.getQuantity(),response.getPaymentMethodType(),response.getCardInfo(),response.getAmount());
    }

    private Membership createSubscribtion(CreateSubsDto createSubsDto) {
        Member member = memberRepository.findById(createSubsDto.getMemberId());
        String subsOrderNo = createSubsDto.getSubsOrderNo();
        String tid = createSubsDto.getTid();
        Amount amount = createSubsDto.getAmout();

        return Membership.create(member,subsOrderNo, tid,amount);
    }

//    private Membership updateMembership(KakaoPaySubsApproveResponse response, Membership membership){
//        String tid = response.getTid();
//        String sid = response.getSid();
//        String cid = response.getCid();
//        String orderNo = response.getPartnerOrderId();
//        String partnerUserId = response.getPartnerUserId();
//        String itemName = response.getOrderName();
//        Long quantity = response.getQuantity();
//        String paymentMethod = response.getPaymentMethodType();
//        CardInfo cardInfo = response.getCardInfo();
//        Amount amount = response.getAmount();
//
//        return Membership.createPayment(tid, sid, cid, orderNo,partnerUserId ,itemName ,quantity, paymentMethod, cardInfo, amount);
//    }

    }

