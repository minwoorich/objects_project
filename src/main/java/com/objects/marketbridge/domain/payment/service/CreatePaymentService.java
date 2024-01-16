package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.objects.marketbridge.domain.payment.domain.TossPaymentsStatus.IN_PROGRESS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreatePaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    @Transactional
    public void create(Long memberId, String paymentKey, String orderNo, Long totalPrice) {
        ProdOrder prodOrder = orderRepository.findByOrderNo(orderNo);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));

        String memberName     = member.getName();
        String phoneNo        = member.getPhoneNo();
        String orderName      = prodOrder.getOrderName();

        Payment payment = Payment.create(memberName, orderName, totalPrice, orderNo, paymentKey, phoneNo, IN_PROGRESS.toString());
        paymentRepository.save(payment);
    }
}
