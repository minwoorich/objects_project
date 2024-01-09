package com.objects.marketbridge.domain.order.controller;

import com.objects.marketbridge.domain.order.controller.response.CheckoutResponse;
import com.objects.marketbridge.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/direct/checkout")
    public ResponseEntity<CheckoutResponse> directInitOrder(
            @RequestParam List<String> items) {

        return null;

    }



}
