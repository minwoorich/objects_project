package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDtoRepository {

    Page<OrderCancelReturnListResponse> findOrdersByMemberId(Long memberId, Pageable pageable);
}
