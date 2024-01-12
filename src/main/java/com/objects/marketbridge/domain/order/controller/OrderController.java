package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.OrderService;
import com.objects.marketbridge.domain.payment.service.TossPaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/direct/checkout")
    public ApiResponse<CheckoutResponse> showCheckout() {
        return null;
    }

    @PostMapping("/orders/toss")
    public ApiResponse<OrderResponse> createOrder(@SessionAttribute Long memberId, @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        CreateProdOrderDto createProdOrderDto = createOrderRequest.toProdOrderDto(memberId);
        List<CreateProdOrderDetailDto> createProdOrderDetailDtos = createOrderRequest.toProdOrderDetailDtos();
        orderService.create(createProdOrderDto, createProdOrderDetailDtos);
        return null;
    }

}
