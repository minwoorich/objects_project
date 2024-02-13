package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.OrderCancelReturn;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.ConfirmRecantationDto;
import com.objects.marketbridge.order.service.dto.ConfirmReturnDto;
import com.objects.marketbridge.order.service.dto.GetReturnDetailDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.order.service.port.OrderCancelReturnCommendRepository;
import com.objects.marketbridge.order.service.port.OrderCancelReturnQueryRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.payment.service.dto.PaymentDto;
import com.objects.marketbridge.payment.service.port.PaymentClient;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_RETURNABLE_PRODUCT;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;


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
                ConfirmReturnDto.Response::of,
                RETURN_INIT.getCode()
        );
    }

    public RequestReturnDto.Response findReturnInfo(Long orderDetailId, Long numberOfReturns, String membership) {
        // TODO fetchJoin으로 변경
        OrderDetail orderDetail = valifyReturnOrderDetail(orderDetailId);

        return RequestReturnDto.Response.of(orderDetail, numberOfReturns, membership);
    }

    public GetReturnDetailDto.Response findReturnDetail(Long orderReturnId, String membership) {
        OrderCancelReturn returnDetail = orderCancelReturnQueryRepository.findById(orderReturnId);

        return GetReturnDetailDto.Response.of(returnDetail, membership, dateTimeHolder);
    }

    @Transactional
    public ConfirmRecantationDto.Response confirmRecantation(Long orderDetailId) {
        OrderDetail orderDetail = valifyReturnOrderDetail(orderDetailId);
        // 내부 재결재
        PaymentDto paymentDto = paymentClient.payment(orderDetail.getOrder(), orderDetail.getOrder().getPayment().getPgToken());
        // 상품 재고 감소
        orderDetail.getProduct().decrease(orderDetail.getReducedQuantity());
        // 쿠폰 상태 변경
        orderDetail.getMemberCoupon().changeUsageInfo(orderDetail.getCancelledAt());
        // orderDetail 상태 변경 (줄어든 수량)
//        orderDetail.changeReducedQuantity();
        // 같은 field의 orderDetail 찾기
//            orderDetailQueryRepository.findByAllField(orderDetail);
        // 만약 부분 반품이었으면 소프트 삭제
        if (orderDetail.getStatusCode() == ORDER_PARTIAL_RETURN.getCode()) {
//            orderDetailCommendRepository. 삭제
//            찾은 orderDetail 줄어든 수량 변경
        } else {
//            줄어든 수량 변경 + 주문 상태 변경
        }
        // 모든 반품이었으면 배송완료로 상태 변경
        return ConfirmRecantationDto.Response.of();
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
