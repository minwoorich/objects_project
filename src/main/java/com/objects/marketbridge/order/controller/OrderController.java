package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.interceptor.PageResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.order.controller.dto.select.GetOrderDetailHttp;
import com.objects.marketbridge.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.order.service.CreateCheckoutService;
import com.objects.marketbridge.order.service.CreateOrderService;
import com.objects.marketbridge.order.service.GetOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.objects.marketbridge.common.config.KakaoPayConfig.ONE_TIME_CID;

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
            @Valid @RequestBody CreateOrderHttp.Request createOrderRequest) {

        // 0. orderNo 생성
        String orderNo = UUID.randomUUID().toString();

        // 1. kakaoPaymentReadyService 호출
        KakaoPayReadyResponse kakaoReadyResponse = createOrderService.ready(createOrderRequest, orderNo, memberId);
        String tid = kakaoReadyResponse.getTid();

        // 2. 주문 생성
        createOrderService.create(createOrderRequest.toDto(orderNo, tid, memberId));

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
