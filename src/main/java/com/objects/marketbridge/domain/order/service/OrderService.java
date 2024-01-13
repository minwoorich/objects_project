package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CreateOrderService createOrderService;
    private final MemberRepository memberRepository;
    private final TossPaymentConfig paymentConfig;

    public CreateOrderResponse create(CreateProdOrderDto prodOrderDto, List<CreateProdOrderDetailDto> prodOrderDetailDtos) {
        createOrderService.orderCreate(prodOrderDto, prodOrderDetailDtos);
        Member member = memberRepository.findById(prodOrderDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));

        String email          = member.getEmail();
        String successUrl     = paymentConfig.getSuccessUrl();
        String failUrl        = paymentConfig.getFailUrl();

        return CreateOrderResponse.from(prodOrderDto, email, successUrl, failUrl);
    }
}
