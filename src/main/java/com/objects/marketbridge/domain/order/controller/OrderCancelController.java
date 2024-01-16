package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.domain.order.service.OrderCancelService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private OrderCancelService orderCancelService;

    @PostMapping("/orders/cancel-flow")
    public ApiResponse<OrderCancelResponse> cancelOrder(
            @RequestParam Long orderId,
            @RequestParam String reason) {

        orderCancelService.orderCancel(orderId, reason);

        return ApiResponse.ok();
    }
}
