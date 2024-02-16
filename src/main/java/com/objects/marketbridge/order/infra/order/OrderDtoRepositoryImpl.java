package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.dto.GetOrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.objects.marketbridge.member.domain.QAddress.address;
import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.payment.domain.QPayment.payment;
import static com.objects.marketbridge.product.domain.QProduct.product;


@Slf4j
@Repository
@Transactional(readOnly = true)
public class OrderDtoRepositoryImpl implements OrderDtoRepository {

    private final OrderJpaRepository orderJpaRepository;

    private final JPAQueryFactory queryFactory;

    public OrderDtoRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager em) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public GetOrderDto findByOrderNo(String orderNo) {
        Order orderEntity = queryFactory
                .selectFrom(order)
                .innerJoin(order.address, address)
                .innerJoin(order.member, member)
                .innerJoin(order.payment, payment).fetchJoin()
                .innerJoin(order.orderDetails, orderDetail).fetchJoin()
                .innerJoin(orderDetail.product, product).fetchJoin()
                .where(
                        eqOrderNo(orderNo)
                )
                .fetchOne();

        // 엔티티 -> dto 로 변환
        assert orderEntity != null;

        return GetOrderDto.of(orderEntity);
    }

    private BooleanExpression eqOrderNo(String orderNo) {
        return order.orderNo.eq(orderNo);
    }
}
