package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;

public interface OrderDtoRepository {

    Page<GetCancelReturnListDtio.Response> findOrdersByMemberId(Long memberId, Pageable pageable);

    Page<OrderDtio> findByMemberIdWithMemberAddress(Condition condition, Pageable pageable);
    Page<OrderDtio> findByMemberIdWithMemberAddressNoFilter(Long memberId, Pageable pageable);

}
