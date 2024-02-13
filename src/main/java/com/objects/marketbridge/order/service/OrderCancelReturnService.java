package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderCancelReturnCommendRepository;
import com.objects.marketbridge.order.service.port.OrderCancelReturnQueryRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.payment.service.port.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public abstract class OrderCancelReturnService {

    protected final PaymentClient paymentClient;

    protected final DateTimeHolder dateTimeHolder;

    protected final OrderDetailQueryRepository orderDetailQueryRepository;
    protected final OrderDetailCommendRepository orderDetailCommendRepository;
    protected final OrderCancelReturnQueryRepository orderCancelReturnQueryRepository;
    private final OrderCancelReturnCommendRepository orderCancelReturnCommendRepository;

    protected <T> T confirmProcess(Long orderDetailId, String reason, Long numberOfOperations, DateTimeHolder dateTimeHolder, OrderDetailOperation operation, ResponseBuilder<T> responseBuilder, String statusCode) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);

        operation.apply(orderDetail, numberOfOperations, dateTimeHolder);

        RefundDto refundDto = paymentClient.refund(getTid(orderDetail), orderDetail.cancelAmount());

        orderCancelReturnCommendRepository.save(OrderCancelReturn.create(orderDetail, statusCode, reason));

        return responseBuilder.build(orderDetail, refundDto);
    }

    private String getTid(OrderDetail orderDetail) {
        return orderDetail.getOrder().getTid();
    }

    @FunctionalInterface
    interface OrderDetailOperation {
        void apply(OrderDetail orderDetail, Long numberOfOperations, DateTimeHolder dateTimeHolder);
    }

    @FunctionalInterface
    interface ResponseBuilder<T> {
        T build(OrderDetail orderDetail, RefundDto refundDto);
    }

}
