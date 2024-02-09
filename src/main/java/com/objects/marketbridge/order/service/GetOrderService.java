package com.objects.marketbridge.order.service;

import com.objects.marketbridge.order.controller.dto.select.GetOrderDetailHttp;
import com.objects.marketbridge.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.objects.marketbridge.order.controller.dto.select.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.controller.dto.select.GetOrderHttp.Response;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetOrderService {

    private final OrderDtoRepository orderDtoRepository;

    public GetOrderHttp.Response search(Pageable pageable, Condition condition) {
        Page<OrderDtio> pagedOrders = orderDtoRepository.findAllPaged(condition, pageable);
        return Response.of(pagedOrders.getContent());
    }

    public GetOrderDetailHttp.Response getOrderDetails(String orderNo) {
        OrderDtio orderDtio = orderDtoRepository.findByOrderNo(orderNo);

        return GetOrderDetailHttp.Response.of(orderDtio);
    }
}
