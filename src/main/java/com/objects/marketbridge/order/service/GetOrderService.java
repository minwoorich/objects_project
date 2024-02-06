package com.objects.marketbridge.order.service;

import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Response;

@Service
@Slf4j
@AllArgsConstructor
public class GetOrderService {

    private final OrderQueryRepository orderQueryRepository;
    private final OrderDtoRepository orderDtoRepository;

    // TODO : 전체 주문 목록 조회 서비스코드 작성
    public Response find(Pageable pageable, Condition condition) {
        return null;
    }
}
