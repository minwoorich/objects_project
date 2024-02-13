package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDetailDtoRepository {
    Page<GetCancelReturnListDtio.Response> findCancelReturnListDtio(Long memberId, Pageable pageable);

}
