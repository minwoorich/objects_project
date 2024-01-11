package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.OrderCreateRequest;
import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.service.OrderService;
import com.objects.marketbridge.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/direct/checkout")
    public ApiResponse<CheckoutResponse> showCheckout() {
        return null;
    }

    @PostMapping("/payment")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        CreateOrderDto orderCreate = CreateOrderDto.from(request);
        orderService.create(orderCreate);
        return null;

    }



}
