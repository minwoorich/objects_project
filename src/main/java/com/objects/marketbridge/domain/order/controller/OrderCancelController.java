package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.request.OrderCancelRequest;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnResponse;
import com.objects.marketbridge.domain.order.service.OrderCancelService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private final OrderCancelService orderCancelService;

    @PostMapping("/orders/cancel-return-flow/thank-you")
    public ApiResponse<OrderCancelReturnResponse> cancelReturnOrder(@RequestBody @Valid OrderCancelRequest request) {
        return ApiResponse.ok(orderCancelService.cancelReturnOrder(request.toServiceRequest(), LocalDateTime.now()));
    }

}
