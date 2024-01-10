package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.model.ProdOrderDetail;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public void create(CreateOrderDto orderDto) {
        ProdOrderDetail prodOrderDetail = ProdOrderDetail.from(orderDto);
        orderDetailRepository.save(prodOrderDetail);
    }
}
