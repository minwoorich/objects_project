package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDtio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDtoRepository {

    Page<CancelReturnResponseDtio> findOrdersByMemberId(Long memberId, Pageable pageable);
}
