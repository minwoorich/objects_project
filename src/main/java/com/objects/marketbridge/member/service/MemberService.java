package com.objects.marketbridge.member.service;

import com.objects.marketbridge.common.domain.*;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.dto.KakaoPaySubsApproveResponse;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.common.security.domain.CustomUserDetails;
import com.objects.marketbridge.common.security.dto.JwtTokenDto;
import com.objects.marketbridge.common.security.service.JwtTokenProvider;
import com.objects.marketbridge.member.dto.CheckedResultDto;
import com.objects.marketbridge.member.dto.CreateSubsDto;
import com.objects.marketbridge.member.dto.SignInDto;
import com.objects.marketbridge.member.dto.SignUpDto;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.member.service.port.MembershipRepository;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public CheckedResultDto isDuplicateEmail(String email){
        boolean isDuplicateEmail = memberRepository.existsByEmail(email);
        return CheckedResultDto.builder().checked(isDuplicateEmail).build();
    }

    @Transactional
    public void save(SignUpDto signUpDto) throws BadRequestException {
        boolean isDuplicateEmail = isDuplicateEmail(signUpDto.getEmail()).isChecked();

        if (isDuplicateEmail) throw new BadRequestException("이미 존재하는 이메일 입니다.");

        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        Member member = signUpDto.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    public JwtTokenDto signIn(SignInDto signInDto) {

        String username = signInDto.getEmail();
        String password = signInDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return jwtTokenProvider.generateToken(principal);
    }

    public void signOut(Long memberId) {
        jwtTokenProvider.deleteToken(memberId);
    }

    public JwtTokenDto reIssueToken(CustomUserDetails principal) {
        return jwtTokenProvider.generateToken(principal);
    }

    @Transactional
    public void changeMemberShip(Long id){

        // 이제 orElseThrow 는 전부 MemberRepositoryImpl 에서 처리하기로 했습니다. by 정민우
//        Member findMember = memberRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id)); // id 를 통한 조회실패 예외발생

        Member findMember = memberRepository.findById(id);

        if(findMember.getMembership().equals("BASIC")){//멤버십 WOW 등록
            findMember.setMembership(MembershipType.WOW.toString());
            memberRepository.save(findMember);
        }else {// 멤버십 BASIC으로 해제
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
        membershipRepository.save(saveSubsResponse(response));
    }

    @Transactional
    public void saveSubsAprrovalResponse(KakaoPaySubsApproveResponse response){
        membershipRepository.save(saveSubscribePaymentResponse(response));
    }

    // Point 도입 하지 않기로해서 해당 내용은 주석 처리 하였습니다 by 정민우
//    public FindPointDto findPointById(Long id){
//        Member findMemberWithPoint=memberRepository.findByIdWithPoint(id)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
//
//        return Point.toDto(findMemberWithPoint);
//    }

    private Membership createSubscribtion(CreateSubsDto createSubsDto) {
        Member member = memberRepository.findById(createSubsDto.getMemberId());
        String subsOrderNo = createSubsDto.getSubsOrderNo();
        String tid = createSubsDto.getTid();
        Amount amount = createSubsDto.getAmout();

        return Membership.create(member,subsOrderNo, tid,amount);
    }

    private Membership saveSubsResponse(KakaoPayApproveResponse response){
        String tid = response.getTid();
        String sid = response.getSid();
        String cid = response.getCid();
        String orderNo = response.getPartnerOrderId();
        String partnerUserId = response.getPartnerUserId();
        String itemName = response.getOrderName();
        Long quantity = response.getQuantity();
        String paymentMethod = response.getPaymentMethodType();
        CardInfo cardInfo = response.getCardInfo();
        Amount amount = response.getAmount();

        return Membership.createPayment(tid, sid, cid, orderNo,partnerUserId ,itemName ,quantity, paymentMethod, cardInfo, amount);
    }

    private Membership saveSubscribePaymentResponse(KakaoPaySubsApproveResponse response){
        String tid = response.getTid();
        String sid = response.getSid();
        String cid = response.getCid();
        String orderNo = response.getPartnerOrderId();
        String partnerUserId = response.getPartnerUserId();
        String itemName = response.getOrderName();
        Long quantity = response.getQuantity();
        String paymentMethod = response.getPaymentMethodType();
        CardInfo cardInfo = response.getCardInfo();
        Amount amount = response.getAmount();

        return Membership.createPayment(tid, sid, cid, orderNo,partnerUserId ,itemName ,quantity, paymentMethod, cardInfo, amount);
    }
}
