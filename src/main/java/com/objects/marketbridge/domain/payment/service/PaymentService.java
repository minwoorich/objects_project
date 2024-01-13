package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(CreateProdOrderDto prodOrderDto) {

        Member member = memberRepository.findById(prodOrderDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));

        String memberName     = member.getName();
        String phoneNo        = member.getPhoneNo();
        String orderName      = prodOrderDto.getOrderName();
        Long totalOrderPrice  = prodOrderDto.getTotalOrderPrice();
        String orderNo        = prodOrderDto.getOrderNo();
        String paymentMethod  = prodOrderDto.getPaymentMethod();

        Payment payment = Payment.create(memberName, orderName, totalOrderPrice, orderNo, paymentMethod, phoneNo);
        paymentRepository.save(payment);

    }

}
