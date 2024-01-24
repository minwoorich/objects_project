package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.global.error.CustomLogicException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductStockService {
    public void decrease(List<OrderDetail> orderDetails) throws CustomLogicException {

        orderDetails.forEach(o -> o.getProduct().decrease(o.getQuantity()));
    }
}
