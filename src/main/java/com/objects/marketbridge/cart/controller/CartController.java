package com.objects.marketbridge.cart.controller;

import com.objects.marketbridge.cart.controller.dto.CreateCartHttp;
import com.objects.marketbridge.cart.service.CreateCartService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CreateCartService createCartService;

    @PostMapping("/carts")
    public ApiResponse<String> createCart(@RequestBody CreateCartHttp.Request request) {
        createCartService.create();
        return ApiResponse.create();
    }
}
