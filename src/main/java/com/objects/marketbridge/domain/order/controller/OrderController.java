package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.order.service.OrderService;
import com.objects.marketbridge.domain.payment.service.PaymentService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/checkout")
    public ApiResponse<?> showCheckout() {
        return null;
    }

    @PostMapping("/orders")
    public ApiResponse<CreateOrderResponse> createOrder(@SessionAttribute Long memberId, @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        String orderNo = UUID.randomUUID().toString();

        CreateOrderResponse resp = orderService.create(
                createOrderRequest.toProdOrderDto(memberId, orderNo),
                createOrderRequest.toProdOrderDetailDtos());
        return ApiResponse.ok(resp);
    }
}
