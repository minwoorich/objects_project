package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.model.ProdOrderDetail;
import com.objects.marketbridge.domain.order.repository.OrderDetailJpaRepository;

import java.util.List;

public interface OrderDetailRepository  {

    int changeAllType(Long orderId, String type);

    List<ProdOrderDetail> saveAll(List<ProdOrderDetail> orderDetail1);
}
