package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.ConfirmReturnDto;
import com.objects.marketbridge.order.service.dto.GetReturnDetailDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.order.service.port.OrderCancelReturnCommendRepository;
import com.objects.marketbridge.order.service.port.OrderCancelReturnQueryRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.payment.service.port.PaymentClient;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_RETURNABLE_PRODUCT;
import static com.objects.marketbridge.order.domain.StatusCodeType.DELIVERY_COMPLETED;


@Service
@Transactional(readOnly = true)
public class OrderReturnService extends OrderCancelReturnService {

    @Builder
    public OrderReturnService(PaymentClient paymentClient, DateTimeHolder dateTimeHolder, OrderDetailQueryRepository orderDetailQueryRepository, OrderDetailCommendRepository orderDetailCommendRepository, OrderCancelReturnQueryRepository orderCancelReturnQueryRepository, OrderCancelReturnCommendRepository orderCancelReturnCommendRepository) {
        super(paymentClient, dateTimeHolder, orderDetailQueryRepository, orderDetailCommendRepository, orderCancelReturnQueryRepository, orderCancelReturnCommendRepository);
    }

    @Transactional
    public ConfirmReturnDto.Response confirmReturn(ConfirmReturnDto.Request request, DateTimeHolder dateTimeHolder) {
        return confirmProcess(
                request.getOrderDetailId(),
                request.getReason(),
                request.getNumberOfReturns(),
                dateTimeHolder,
                OrderDetail::returns,
                ConfirmReturnDto.Response::of
        );
    }

    public RequestReturnDto.Response findReturnInfo(Long orderDetailId, Long numberOfReturns, String membership) {
        // TODO fetchJoin으로 변경
        OrderDetail orderDetail = valifyReturnOrderDetail(orderDetailId);

        return RequestReturnDto.Response.of(orderDetail, numberOfReturns, membership);
    }

    public GetReturnDetailDto.Response findReturnDetail(Long returnedOrderDetailId, String membership) {
        OrderCancelReturn returnDetail = orderCancelReturnQueryRepository.findById(returnedOrderDetailId);

        return GetReturnDetailDto.Response.of(returnDetail, membership, dateTimeHolder);
    }

    private OrderDetail valifyReturnOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);
        if(returnImpossible(orderDetail)) {
            throw CustomLogicException.createBadRequestError(NON_RETURNABLE_PRODUCT);
        }
        return orderDetail;
    }

    private boolean returnImpossible(OrderDetail orderDetail) {
        return !Objects.equals(orderDetail.getStatusCode(), DELIVERY_COMPLETED.getCode());
    }

}
