package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.PaymentResponse;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.OrderService;
import com.objects.marketbridge.domain.payment.service.PaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/orders/checkout")
    public ApiResponse<?> showCheckout() {
        return null;
    }

    @PostMapping("/orders")
    public ApiResponse<PaymentResponse> createOrder(@SessionAttribute Long memberId, @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        String orderNo = UUID.randomUUID().toString();
        CreateProdOrderDto createProdOrderDto = createOrderRequest.toProdOrderDto(memberId, orderNo);
        List<CreateProdOrderDetailDto> createProdOrderDetailDtos = createOrderRequest.toProdOrderDetailDtos();
        orderService.create(createProdOrderDto, createProdOrderDetailDtos);
        return null;
    }
}
