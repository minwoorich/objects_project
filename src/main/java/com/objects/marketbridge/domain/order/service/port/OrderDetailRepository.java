package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.model.ProdOrderDetail;

import java.util.List;

public interface OrderDetailRepository  {

    int changeAllType(Long orderId, String type);

    List<ProdOrderDetail> saveAll(List<ProdOrderDetail> orderDetail1);

    void addReason(Long orderId, String reason);

    void save(ProdOrderDetail prodOrderDetail);
}
