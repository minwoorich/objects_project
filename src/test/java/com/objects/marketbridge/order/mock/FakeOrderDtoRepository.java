package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.order.controller.dto.select.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.infra.dtio.OrderDtio;
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
    public Page<GetCancelReturnListDtio.Response> findOrdersByMemberId(Long memberId, Pageable pageable) {
        List<GetCancelReturnListDtio.Response> content = getOrderCancelReturnListResponses(memberId);

        return PageableExecutionUtils.getPage(content, pageable, content::size);
    }

    private List<GetCancelReturnListDtio.Response> getOrderCancelReturnListResponses(Long memberId) {

        return getInstance().getData().stream()
                .filter(order -> order.getMember().getId().equals(memberId))
                .map(FakeOrderDtoRepository::getReturnResponseDtio)
                .toList();
    }

    private static GetCancelReturnListDtio.Response getReturnResponseDtio(Order order) {
        GetCancelReturnListDtio.Response result = getCancelReturnResponseDtio(order);

        List<GetCancelReturnListDtio.OrderDetailInfo> detailList = order.getOrderDetails().stream()
                .filter(orderDetailStatusCond())
                .map(FakeOrderDtoRepository::getDetailResponseDtio)
                .toList();

        result.changeOrderDetailInfos(detailList);

        return result;
    }

    private static GetCancelReturnListDtio.Response getCancelReturnResponseDtio(Order order) {
        return GetCancelReturnListDtio.Response.builder()
                .orderNo(order.getOrderNo())
                .orderDate(order.getCreatedAt())
                .cancelReceiptDate(order.getUpdatedAt())
                .build();
    }

    private static Predicate<OrderDetail> orderDetailStatusCond() {
        return od -> od.getStatusCode().equals(ORDER_CANCEL.getCode())
                || od.getStatusCode().equals(RETURN_COMPLETED.getCode());
    }

    private static GetCancelReturnListDtio.OrderDetailInfo getDetailResponseDtio(OrderDetail orderDetail) {
        return GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo(orderDetail.getOrderNo())
                .productId(orderDetail.getProduct().getId())
                .productNo(orderDetail.getProduct().getProductNo())
                .name(orderDetail.getProduct().getName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .orderStatus(orderDetail.getStatusCode())
                .build();
    }

    @Override
    public Page<OrderDtio> findAllPaged(GetOrderHttp.Condition condition, Pageable pageable) {
        // TODO : 구현해야함 - 민우
        return null;
    }
}
