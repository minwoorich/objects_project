package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDtoRepository {

    Page<GetCancelReturnListDtio.Response> findOrdersByMemberId(Long memberId, Pageable pageable);
}
