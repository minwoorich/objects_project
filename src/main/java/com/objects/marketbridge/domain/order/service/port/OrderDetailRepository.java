package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;

import java.util.List;

public interface OrderDetailRepository  {

    int changeAllType(Long orderId, String type);

    List<ProdOrderDetail> saveAll(List<ProdOrderDetail> orderDetail);

    void addReason(Long orderId, String reason);

    void deleteAllInBatch();

    void save(ProdOrderDetail prodOrderDetail);

    ProdOrderDetail findById(Long id);

    List<ProdOrderDetail> findByProductId(Long id);

    List<ProdOrderDetail> findAll();

    ProdOrderDetail findByStockIdAndOrderId(Long stockId, Long orderId);
}
