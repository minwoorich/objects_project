package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
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
        KakaoPayReadyRequest kakaoReadyRequest = createKakaoReadyRequest(orderNo, createOrderRequest, memberId);
        KakaoPayReadyResponse kakaoReadyResponse = kakaoPayService.ready(kakaoReadyRequest);
        String tid = kakaoReadyResponse.getTid();

        // 2. 주문 생성
        createOrderService.create(createOrderRequest.toDto(orderNo, tid, memberId));

        return ApiResponse.ok(CreateOrderHttp.Response.of(kakaoReadyResponse));
    }

    private KakaoPayReadyRequest createKakaoReadyRequest(String orderNo, CreateOrderHttp.Request request, Long memberId) {

        String cid = ONE_TIME_CID;
        String cancelUrl = kakaoPayConfig.getRedirectCancelUrl();
        String failUrl = kakaoPayConfig.getRedirectFailUrl();
        String approvalUrl = kakaoPayConfig.createApprovalUrl("/payment");

        return request.toKakaoReadyRequest(orderNo, memberId, cid, approvalUrl, failUrl, cancelUrl);
    }

    @GetMapping("/orders")
    public ApiResponse<GetOrderHttp.Response> getOrders(
            @AuthMemberId Long memberId,
            @RequestParam(name = "year") String year,
            @RequestParam(name = "keyword") String keyword,
            @PageableDefault(value = 5, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {

        GetOrderHttp.Response response = getOrderService.search(pageable, createCondition(year, keyword, memberId));

        return ApiResponse.ok(response);
    }

    private GetOrderHttp.Condition createCondition(String year, String keyword, Long memberId) {
        return GetOrderHttp.Condition.builder()
                .keyword(keyword)
                .year(year)
                .memberId(memberId)
                .build();
    }

    @GetMapping("/orders/{orderNo}")
    public ApiResponse<GetOrderHttp.Response> getOrderDetails(
            @AuthMemberId Long memberId){


        return ApiResponse.ok(null);
    }
}
