package com.objects.marketbridge.order.controller;


import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.AuthMemberId;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.*;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Builder
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderCancelReturnController {

    private final OrderCancelReturnService orderCancelReturnService;
    private final OrderDtoRepository orderDtoRepository;
    private final MemberRepository memberRepository;
    private final DateTimeHolder dateTimeHolder;

    @PostMapping("/cancel-return-flow/thank-you")
    public ApiResponse<ConfirmCancelReturnHttp.Response> confirmCancelReturn(
            @RequestBody @Valid ConfirmCancelReturnHttp.Request request
    ) {
        return ApiResponse.ok(ConfirmCancelReturnHttp.Response.of(orderCancelReturnService.confirmCancelReturn(request.toDto(), dateTimeHolder)));
    }

    @GetMapping("/cancel-flow")
    public ApiResponse<RequestCancelHttp.Response> requestCancel(
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @RequestParam(name = "numberOfCancellation") Long numberOfCancellation,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(RequestCancelHttp.Response.of(orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership)));
    }

    @GetMapping("/return-flow")
    public ApiResponse<RequestReturnHttp.Response> requestReturn (
            @RequestParam(name = "orderDetailId") Long orderDetailId,
            @RequestParam(name = "numberOfReturns") Long numberOfReturns,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(RequestReturnHttp.Response.of(orderCancelReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership)));
    }

    @GetMapping("/cancel-return/list")
    public ApiResponse<Page<GetCancelReturnListHttp.Response>> getCancelReturnList (
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResponse.ok(GetCancelReturnListHttp.Response.of(orderDtoRepository.findOrdersByMemberId(memberId, pageRequest)));
    }

    @GetMapping("/cancel-return/{orderNo}")
    public ApiResponse<GetCancelReturnDetailHttp.Response> getCancelReturnDetail(
            @RequestParam(name = "orderDetailIds") List<Long> orderDetailIds,
            @AuthMemberId Long memberId
    ) {
        String membership = memberRepository.findById(memberId).getMembership();
        return ApiResponse.ok(GetCancelReturnDetailHttp.Response.of(orderCancelReturnService.findCancelReturnDetail(orderDetailIds, membership, dateTimeHolder)));
    }

    @PostMapping("/cancel-return/list")
    public ApiResponse<ReturnRecantationHttp.Response> returnRecantation() {
        return null;
    }


}
