package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.ConfirmCancelDto;
import com.objects.marketbridge.order.service.dto.GetCancelDetailDto;
import com.objects.marketbridge.order.service.dto.RequestCancelDto;
import com.objects.marketbridge.order.service.port.OrderCancelReturnCommendRepository;
import com.objects.marketbridge.order.service.port.OrderCancelReturnQueryRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.payment.service.port.PaymentClient;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_CANCELLABLE_PRODUCT;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;


@Service
@Transactional(readOnly = true)
public class OrderCancelService extends OrderCancelReturnService {

    @Builder
    public OrderCancelService(PaymentClient paymentClient, DateTimeHolder dateTimeHolder, OrderDetailQueryRepository orderDetailQueryRepository, OrderDetailCommendRepository orderDetailCommendRepository, OrderCancelReturnQueryRepository orderCancelReturnQueryRepository, OrderCancelReturnCommendRepository orderCancelReturnCommendRepository) {
        super(paymentClient, dateTimeHolder, orderDetailQueryRepository, orderDetailCommendRepository, orderCancelReturnQueryRepository, orderCancelReturnCommendRepository);
    }

    @Transactional
    public ConfirmCancelDto.Response confirmCancel(ConfirmCancelDto.Request request, DateTimeHolder dateTimeHolder) {
        return confirmProcess(
                request.getOrderDetailId(),
                request.getReason(),
                request.getNumberOfCancellation(),
                dateTimeHolder,
                OrderDetail::cancel,
                ConfirmCancelDto.Response::of
        );
    }

    public RequestCancelDto.Response findCancelInfo(Long orderDetailId, Long numberOfCancellation, String membership) {
        // TODO fetchJoin으로 변경 (Product 까지)
        OrderDetail orderDetail = valifyCancelOrderDetail(orderDetailId);

        return RequestCancelDto.Response.of(orderDetail, numberOfCancellation, membership);
    }

    public GetCancelDetailDto.Response findCancelDetail(Long cancelledOrderDetailId, String membership) {
        OrderCancelReturn cancelDetail = orderCancelReturnQueryRepository.findById(cancelledOrderDetailId);

        return GetCancelDetailDto.Response.of(cancelDetail, membership, dateTimeHolder);
    }

    private OrderDetail valifyCancelOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);
        if(cancelImpossible(orderDetail)) {
            throw CustomLogicException.createBadRequestError(NON_CANCELLABLE_PRODUCT);
        }
        return orderDetail;
    }

    private boolean cancelImpossible(OrderDetail orderDetail) {
        return Objects.equals(orderDetail.getStatusCode(), DELIVERY_COMPLETED.getCode());
    }

}
