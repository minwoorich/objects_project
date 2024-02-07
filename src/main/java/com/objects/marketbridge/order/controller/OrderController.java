package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.config.KakaoPayConfig;
import com.objects.marketbridge.common.dto.KakaoPayReadyRequest;
import com.objects.marketbridge.common.dto.KakaoPayReadyResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.order.controller.dto.CreateCheckoutHttp;
import com.objects.marketbridge.order.controller.dto.CreateOrderHttp;
import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.service.CreateCheckoutService;
import com.objects.marketbridge.order.service.CreateOrderService;
import com.objects.marketbridge.order.service.GetOrderService;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

    // TODO : 이거 api search랑 findall 두개로 나눠야함
    @GetMapping("/orders/list")
    public ApiResponse<GetOrderHttp.Response> getOrders(
            @AuthMemberId Long memberId,
            @RequestParam(name = "isSearch") @NotNull Boolean isSearch,
            @RequestParam(name = "year") String year,
            @RequestParam(name = "keyword") String keyword,
            @PageableDefault(value = 5) Pageable pageable) {

        // 1. 조건들을 담은 condition 객체 생성
        GetOrderHttp.Condition condition = createCondition(isSearch, year, keyword, memberId);

        // 2. 검색 일 경우
        if (condition.getIsSearch()) {
            log.info("search 호출");
            return ApiResponse.ok(getOrderService.search(pageable, condition));
        }

        // 3. 그냥 전체 조회일 경우
        log.info("findAll 호출");
        return ApiResponse.ok(getOrderService.findAll(pageable, memberId));
    }

    private GetOrderHttp.Condition createCondition(Boolean isSearch, String year, String keyword, Long memberId) {
        return GetOrderHttp.Condition.builder()
                .keyword(keyword)
                .year(year)
                .isSearch(isSearch)
                .memberId(memberId)
                .build();
    }
}
