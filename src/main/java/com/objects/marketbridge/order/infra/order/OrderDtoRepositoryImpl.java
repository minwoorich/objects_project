package com.objects.marketbridge.order.infra.order;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_OrderDetailInfo;
import com.objects.marketbridge.order.infra.dtio.QGetCancelReturnListDtio_Response;
import com.objects.marketbridge.order.service.dto.OrderDetailDto;
import com.objects.marketbridge.order.service.dto.OrderDto;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.Condition;
import static com.objects.marketbridge.order.domain.QAddress.address;
import static com.objects.marketbridge.order.domain.QOrder.order;
import static com.objects.marketbridge.order.domain.QOrderDetail.orderDetail;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;
import static com.objects.marketbridge.product.domain.QProduct.product;
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
    @Transactional(readOnly = true)
    public Page<GetCancelReturnListDtio.Response> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<GetCancelReturnListDtio.Response> content = getOrderCancelReturnListResponses(memberId);
        Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> orderDetailResponseMap = getOrderDetailResponseMap(findOrderNos(content));
        orderDetailResponseSetting(content, orderDetailResponseMap);

        JPAQuery<GetCancelReturnListDtio.Response> countQuery = getCountQuery(memberId);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<GetCancelReturnListDtio.Response> getOrderCancelReturnListResponses(Long memberId) {
        List<GetCancelReturnListDtio.Response> content = queryFactory
                .select(
                        new QGetCancelReturnListDtio_Response (
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId))
                .fetch();
        return content;
    }

    private List<String> findOrderNos(List<GetCancelReturnListDtio.Response> content) {
        List<String> toOrderNos = content.stream()
                .map(GetCancelReturnListDtio.Response::getOrderNo)
                .toList();
        return toOrderNos;
    }

    private Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> getOrderDetailResponseMap(List<String> toOrderIds) {
        List<GetCancelReturnListDtio.OrderDetailInfo> detailResponseDtioList = queryFactory
                .select(
                        new QGetCancelReturnListDtio_OrderDetailInfo(
                                orderDetail.orderNo,
                                orderDetail.product.id,
                                orderDetail.product.productNo,
                                orderDetail.product.name,
                                orderDetail.product.price,
                                orderDetail.quantity,
                                orderDetail.statusCode
                        ))
                .from(orderDetail)
                .join(orderDetail.product, product)
                .where(
                        orderDetail.order.orderNo.in(toOrderIds),
                        orderDetail.statusCode.eq(ORDER_CANCEL.getCode())
                                .or(orderDetail.statusCode.eq(RETURN_COMPLETED.getCode()))
                ).fetch();


        return detailResponseDtioList.stream()
                .collect(Collectors.groupingBy(GetCancelReturnListDtio.OrderDetailInfo::getOrderNo));
    }

    private void orderDetailResponseSetting(List<GetCancelReturnListDtio.Response> content, Map<String, List<GetCancelReturnListDtio.OrderDetailInfo>> orderDetailResponseMap) {
        content.forEach(o -> o.changeOrderDetailInfos(orderDetailResponseMap.get(o.getOrderNo())));
    }

    private JPAQuery<GetCancelReturnListDtio.Response> getCountQuery(Long memberId) {
        JPAQuery<GetCancelReturnListDtio.Response> countQuery = queryFactory
                .select(new QGetCancelReturnListDtio_Response(
                                order.updatedAt,
                                order.createdAt,
                                order.orderNo
                        )
                ).from(order)
                .where(order.member.id.eq(memberId));
        return countQuery;
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 엔티티 -> dto 로 변환
        List<OrderDto> orderDtos = orders.stream().map(OrderDto::of).toList();
        log.info("orderDtos 사이즈 : {}", orderDtos.size());

        // orderDtos -> keyword 로 필터링
        List<OrderDto> filteredOrderDtos = filterByKeyword(condition.getKeyword(), orderDtos);
        log.info("filteredOrderDtos 사이즈 : {}", filteredOrderDtos.size());
        log.info("filteredOrderDtos.getOrderDetails 사이즈 : {}", filteredOrderDtos.get(0).getOrderDetails().size());
        log.info("filteredOrderDtos.getOrderDetails.product이름1 : {}", filteredOrderDtos.get(0).getOrderDetails().get(0).getProduct().getName());
        log.info("filteredOrderDtos.getOrderDetails.product이름2 : {}", filteredOrderDtos.get(0).getOrderDetails().get(1).getProduct().getName());
        log.info("filteredOrderDtos.getOrderDetails.product이름3 : {}", filteredOrderDtos.get(0).getOrderDetails().get(2).getProduct().getName());

        // 카운트 쿼리
        JPAQuery<Long> countQuery = createCountOrdersQuery(condition);

        return PageableExecutionUtils.getPage(filteredOrderDtos, pageable, countQuery::fetchOne);
    }

    // TODO : private -> public 으로 해놓고 filterByKeyword 테스트 코드 작성해봐야함
    // TODO : 계속 이부분 관련해서 테스트 실패 발생
    // TODO : 파라미터로 주입받는 orderDtos 가 이미 사이즈가 1이기 때문에 문제발생. filter -> 페이징 순으로 해야 에러 해결할듯
    private List<OrderDto> filterByKeyword(String keyword, List<OrderDto> orderDtos) {
        // keyword 있으면 필터링 한것 반환, 없으면 그냥 그대로 orderDtos 반환
        return StringUtils.hasText(keyword) ?
                orderDtos.stream()
                .filter(orderDto ->
                        // OrderDto의 OrderDetailsDto 리스트에서 ProductDto의 이름이 키워드를 포함하는지 여부 확인
                        orderDto.getOrderDetails().stream()
                                .anyMatch(orderDetailDto ->
                                        orderDetailDto.getProduct().getName().contains(keyword))).toList()
            :orderDtos;
    }

    private JPAQuery<Long> createCountOrdersQuery(Condition condition) {
        return queryFactory
                .select(order.id.countDistinct())
                .from(order)
                .innerJoin(order.orderDetails, orderDetail)
                .innerJoin(orderDetail.product, product)
                .where(
                        eqYear(condition.getYear()),
                        eqMemberId(condition.getMemberId()),
                        containsKeyword(condition.getKeyword())
                );
    }

    private BooleanExpression eqYear(String year) {
        return hasText(year) ? order.createdAt.year().eq(Integer.parseInt(year)) : null;
    }

    private BooleanExpression containsKeyword(String keyword) {
        return hasText(keyword) ? orderDetail.product.name.like(keyword) : null;
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return order.member.id.eq(memberId);
    }

}
