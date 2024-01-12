package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.service.OrderService;
import com.objects.marketbridge.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TossPayController {

    private final OrderService orderService;


}
