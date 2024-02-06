package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.product.domain.Product;

import java.util.List;

public interface OrderDetailQueryRepository {

    OrderDetail findById(Long id);

    List<OrderDetail> findByProductId(Long id);

    List<OrderDetail> findAll();

    List<OrderDetail> findByOrderNo(String orderNo);

    List<OrderDetail> findByOrder_IdAndProductIn(Long orderId, List<Product> products);

    List<OrderDetail> findByOrderNoAndProduct_IdIn(String orderNo, List<Long> productIds);

    List<OrderDetail> findByIdIn(List<Long> orderDetailIds);
}
