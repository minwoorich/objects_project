package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.order.service.dto.GetOrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;

public class FakeOrderDtoRepository extends BaseFakeOrderRepository implements OrderDtoRepository {

    @Override
    public GetOrderDto findByOrderNo(String orderNo) {
        // TODO : 구현해야함 - 민우
        return null;
    }
}
