package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.dto.OrderCancelRequestDto;
import com.objects.marketbridge.domain.order.dto.OrderCancelResponseDto;
import com.objects.marketbridge.domain.order.service.OrderCancelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private OrderCancelService orderCancelService;

    @GetMapping("/orders/cancel-flow")
    public OrderCancelResponseDto cancelOrder(@Valid @RequestBody OrderCancelRequestDto request) {
        return new OrderCancelResponseDto();
    }

}
