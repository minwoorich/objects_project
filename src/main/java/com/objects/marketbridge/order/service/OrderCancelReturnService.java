package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.payment.service.port.RefundClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_PARTIAL_CANCEL;


@Service
@RequiredArgsConstructor
public abstract class OrderCancelReturnService {

    private final RefundClient refundClient;

    protected final DateTimeHolder dateTimeHolder;

    protected final OrderDetailQueryRepository orderDetailQueryRepository;
    protected final OrderDetailCommendRepository orderDetailCommendRepository;

    protected <T> T processOrderDetail(Long orderDetailId, String reason, Long numberOfOperations, DateTimeHolder dateTimeHolder, OrderDetailOperation operation, ResponseBuilder<T> responseBuilder) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);

        boolean isPartialOperation = operation.apply(orderDetail, reason, numberOfOperations, dateTimeHolder);

        RefundDto refundDto = refundClient.refund(getTid(orderDetail), orderDetail.cancelAmount());

        if (isPartialOperation) {
            OrderDetail savedOrderDetail = orderDetailCommendRepository.saveAndReturnEntity(OrderDetail.create(orderDetail, reason, ORDER_PARTIAL_CANCEL.getCode()));
            return responseBuilder.build(savedOrderDetail, refundDto);
        }

        return responseBuilder.build(orderDetail, refundDto);
    }

    private String getTid(OrderDetail orderDetail) {
        return orderDetail.getOrder().getTid();
    }

    @FunctionalInterface
    interface OrderDetailOperation {
        boolean apply(OrderDetail orderDetail, String reason, Long numberOfOperations, DateTimeHolder dateTimeHolder);
    }

    @FunctionalInterface
    interface ResponseBuilder<T> {
        T build(OrderDetail orderDetail, RefundDto refundDto);
    }

}
