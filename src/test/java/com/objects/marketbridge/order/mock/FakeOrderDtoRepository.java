package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FakeOrderDtoRepository extends BaseFakeOrderRepository implements OrderDtoRepository {

    @Override
    public Page<OrderDtio> findAllPaged(GetOrderHttp.Condition condition, Pageable pageable) {
        // TODO : 구현해야함 - 민우
        return null;
    }

    @Override
    public OrderDtio findByOrderNo(String orderNo) {
        // TODO : 구현해야함 - 민우
        return null;
    }
}
