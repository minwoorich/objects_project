package com.objects.marketbridge.domains.order.controller;

import com.objects.marketbridge.common.kakao.KakaoPayConfig;
import com.objects.marketbridge.common.kakao.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.kakao.KakaoPayService;
import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.common.responseobj.PageResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.domains.order.service.CreateCheckoutService;
import com.objects.marketbridge.domains.order.service.CreateOrderService;
import com.objects.marketbridge.domains.order.service.GetOrderService;
import com.objects.marketbridge.domains.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.domains.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.domains.order.controller.dto.select.GetOrderDetailHttp;
import com.objects.marketbridge.domains.order.controller.dto.select.GetOrderHttp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CreateOrderService createOrderService;
    private final CreateCheckoutService createCheckoutService;
    private final GetOrderService getOrderService;
    private final KakaoPayService kakaoPayService;
    private final KakaoPayConfig kakaoPayConfig;

    @GetMapping("/orders/checkout")
    public ApiResponse<CreateCheckoutHttp.Response> createCheckout(
            @AuthMemberId Long memberId) {

        return ApiResponse.ok(createCheckoutService.create(memberId));
    }

    @PostMapping("/orders/checkout")
    public ApiResponse<CreateOrderHttp.Response> createOrder(
            @AuthMemberId Long memberId,
            @Valid @RequestBody CreateOrderHttp.Request request) {

        // 0. orderNo 생성
        String orderNo = UUID.randomUUID().toString();

        // 1. 카카오페이 결제 준비 요청
        KakaoPayReadyResponse kakaoReadyResponse = createOrderService.ready(request, orderNo, memberId);
        String tid = kakaoReadyResponse.getTid();

        // 2. 주문 생성
        createOrderService.create(request.toDto(orderNo, tid, memberId));

        return ApiResponse.ok(CreateOrderHttp.Response.of(kakaoReadyResponse));
    }

    @GetMapping("/orders")
    public ApiResponse<PageResponse<GetOrderHttp.Response>> getOrders(
            @AuthMemberId Long memberId,
            @RequestParam(name = "year") String year,
            @RequestParam(name = "keyword") String keyword,
            @PageableDefault(value = 5, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.ok(getOrderService.search(pageable, createCondition(year, keyword, memberId)));
    }

    private GetOrderHttp.Condition createCondition(String year, String keyword, Long memberId) {
        return GetOrderHttp.Condition.builder()
                .keyword(keyword)
                .year(year)
                .memberId(memberId)
                .build();
    }

    @UserAuthorize
    @GetMapping("/orders/{orderId}")
    public ApiResponse<GetOrderDetailHttp.Response> getOrderDetails(
            @PathVariable(name = "orderId") Long orderId){

        return ApiResponse.ok(getOrderService.getOrderDetails(orderId));
    }
}
