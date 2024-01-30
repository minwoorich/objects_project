package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @Autowired
    OrderCommendRepository orderCommendRepository;

    @PostMapping("/hi")
    public void test(@RequestParam(name = "orderName") String orderName) {
        Order order = Order.builder().orderName(orderName).build();
        orderCommendRepository.save(order);
    }
}
