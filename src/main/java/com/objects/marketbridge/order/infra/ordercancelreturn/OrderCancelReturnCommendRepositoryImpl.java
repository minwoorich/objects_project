package com.objects.marketbridge.order.infra.ordercancelreturn;

import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.order.service.port.OrderCancelReturnCommendRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCancelReturnCommendRepositoryImpl implements OrderCancelReturnCommendRepository {

    private final OrderCancelReturnJpaRepository orderCancelReturnJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderCancelReturnCommendRepositoryImpl(OrderCancelReturnJpaRepository orderCancelReturnJpaRepository, EntityManager em) {
        this.orderCancelReturnJpaRepository = orderCancelReturnJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public OrderCancelReturn save(OrderCancelReturn orderCancelReturn) {
        return orderCancelReturnJpaRepository.save(orderCancelReturn);
    }
}
