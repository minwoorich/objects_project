package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.service.dto.OrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FakeOrderDtoRepository extends BaseFakeOrderRepository implements OrderDtoRepository {

    @Override
    public Page<OrderDto> findByMemberIdWithMemberAddress(GetOrderHttp.Condition condition, Pageable pageable) {
        // TODO : 구현해야함 - 민우
        return null;
    }
}
