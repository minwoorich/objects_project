package com.objects.marketbridge.order.service;

import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Response;

@Service
@Slf4j
@AllArgsConstructor
public class GetOrderService {

    private final OrderDtoRepository orderDtoRepository;

    // TODO : 전체 주문 목록 조회 서비스코드 작성
    public Response search(Pageable pageable, Condition condition) {
        Page<OrderDtio> pagedOrders = orderDtoRepository.findByMemberIdWithMemberAddress(condition, pageable);
        return Response.of(pagedOrders.getContent());
    }

    public Response findAll(Pageable pageable, Long memberId) {
        Page<OrderDtio> pagedOrders = orderDtoRepository.findByMemberIdWithMemberAddressNoFilter(memberId, pageable);
        return Response.of(pagedOrders.getContent());
    }
}
