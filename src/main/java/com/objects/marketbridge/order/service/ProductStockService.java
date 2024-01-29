package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.exception.error.CustomLogicException;
import com.objects.marketbridge.order.domain.OrderDetail;
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
