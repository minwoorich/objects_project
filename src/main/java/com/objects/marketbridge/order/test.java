package com.objects.marketbridge.order;

import com.objects.marketbridge.order.domain.Order;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class test {
    @Autowired
    static EntityManager em;

    public static void main(String[] args) {
        Order order = Order.builder().build();
        em.persist(order);
        em.flush();
    }
}
