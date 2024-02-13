package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.objects.marketbridge.order.controller.dto.select.GetOrderHttp.Condition;

public interface OrderDtoRepository {


    Page<OrderDtio> findAllPaged(Condition condition, Pageable pageable);

    OrderDtio findByOrderNo(String orderNo);

}
