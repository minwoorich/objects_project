package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.dto.OrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.domain.QAddress.address;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static org.springframework.util.StringUtils.hasText;


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
    public Page<OrderDto> findByMemberIdWithMemberAddress(Condition condition, Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.address, address).fetchJoin()
                .join(order.member, member).fetchJoin()
                .where(
                        eqMemberId(condition.getMemberId()),
                        eqYear(condition.getYear())
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();

        // 엔티티 -> dto 로 변환
        List<OrderDto> orderDtos = orders.stream().map(OrderDto::of).toList();

        return new PageImpl<>(orderDtos, pageable, orderDtos.size());
    }

    private BooleanExpression eqYear(String year) {
        return hasText(year) ? order.createdAt.year().eq(Integer.parseInt(year)) : null;
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return order.member.id.eq(memberId);
    }
}
