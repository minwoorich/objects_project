package com.objects.marketbridge.mock;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDtio;
import com.objects.marketbridge.order.infra.dtio.DetailResponseDtio;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.function.Predicate;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static com.objects.marketbridge.order.domain.StatusCodeType.RETURN_COMPLETED;

public class FakeOrderDtoRepository extends BaseFakeOrderRepository implements OrderDtoRepository {
    @Override
    public Page<CancelReturnResponseDtio> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<CancelReturnResponseDtio> content = getOrderCancelReturnListResponses(memberId);

        return PageableExecutionUtils.getPage(content, pageable, content::size);
    }

    private List<CancelReturnResponseDtio> getOrderCancelReturnListResponses(Long memberId) {

        return getInstance().getData().stream()
                .filter(order -> order.getMember().getId().equals(memberId))
                .map(FakeOrderDtoRepository::getReturnResponseDtio)
                .toList();
    }

    private static CancelReturnResponseDtio getReturnResponseDtio(Order order) {
        CancelReturnResponseDtio result = getCancelReturnResponseDtio(order);

        List<DetailResponseDtio> detailList = order.getOrderDetails().stream()
                .filter(orderDetailStatusCond())
                .map(FakeOrderDtoRepository::getDetailResponseDtio)
                .toList();

        result.changeDetailResponsDaos(detailList);

        return result;
    }

    private static CancelReturnResponseDtio getCancelReturnResponseDtio(Order order) {
        return CancelReturnResponseDtio.builder()
                .orderNo(order.getOrderNo())
                .orderDate(order.getCreatedAt())
                .cancelReceiptDate(order.getUpdatedAt())
                .build();
    }

    private static Predicate<OrderDetail> orderDetailStatusCond() {
        return od -> od.getStatusCode().equals(ORDER_CANCEL.getCode())
                || od.getStatusCode().equals(RETURN_COMPLETED.getCode());
    }

    private static DetailResponseDtio getDetailResponseDtio(OrderDetail orderDetail) {
        return DetailResponseDtio.builder()
                .orderNo(orderDetail.getOrderNo())
                .productId(orderDetail.getProduct().getId())
                .productNo(orderDetail.getProduct().getProductNo())
                .name(orderDetail.getProduct().getName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .orderStatus(orderDetail.getStatusCode())
                .build();
    }

}
