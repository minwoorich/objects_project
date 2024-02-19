package com.objects.marketbridge.order.controller;


import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.*;
import com.objects.marketbridge.order.service.OrderCancelService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderCancelController {

    private final OrderCancelService orderCancelService;
    private final MemberRepository memberRepository;
    private final DateTimeHolder dateTimeHolder;

    @PostMapping("/cancel-flow/thank-you")
    public ApiResponse<ConfirmCancelHttp.Response> confirmCancel(
            @RequestBody @Valid ConfirmCancelHttp.Request request
    ) {
        return ApiResponse.ok(ConfirmCancelHttp.Response.of(orderCancelService.confirmCancel(request.toDto(), dateTimeHolder)));
    }

    @GetMapping("/cancel-flow")
    public ApiResponse<RequestCancelHttp.Response> requestCancel(
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @RequestParam(name = "numberOfCancellation") Long numberOfCancellation,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(RequestCancelHttp.Response.of(orderCancelService.findCancelInfo(orderDetailId, numberOfCancellation, membership)));
    }

    @GetMapping("/cancel/detail")
    public ApiResponse<GetCancelDetailHttp.Response> getCancelDetail(
            @RequestParam(name = "cancelledOrderDetailId") Long cancelledOrderDetailId,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(GetCancelDetailHttp.Response.of(orderCancelService.findCancelDetail(cancelledOrderDetailId, membership)));
    }
}
