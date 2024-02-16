package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.order.service.dto.GetOrderDto;

public interface OrderDtoRepository {


    GetOrderDto findByOrderNo(String orderNo);

}
