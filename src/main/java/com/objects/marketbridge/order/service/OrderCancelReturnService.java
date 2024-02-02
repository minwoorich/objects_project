package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.*;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
public class OrderCancelReturnService {

    private final DateTimeHolder dateTimeHolder;

    private final RefundService refundService;

    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final ProductRepository productRepository;


    // TODO 트랜잭션 위치 고려해야함
    @Transactional
    public ConfirmCancelReturnDto.Response confirmCancelReturn(ConfirmCancelReturnDto.Request request, DateTimeHolder dateTimeHolder) {
        Order order = orderQueryRepository.findByOrderNo(request.getOrderNo());

        Integer cancelAmount = order.changeDetailsReasonAndStatus(request.getCancelReason(), ORDER_CANCEL.getCode());

        order.changeMemberCouponInfo(null);

        RefundDto refundDto = refundService.refund(order.getTid(), cancelAmount);

        return ConfirmCancelReturnDto.Response.of(order, refundDto, dateTimeHolder);
    }

    public RequestCancelDto.Response findCancelInfo(String orderNo, List<Long> productIds, String membership) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);

        return RequestCancelDto.Response.of(orderDetails, membership); // TODO 맴버 조회해서 타입 넣기
    }

    public ReturnResponseDto requestReturn(String orderNo, List<Long> productIds, String membership) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);

        return ReturnResponseDto.of(orderDetails, membership); // TODO 맴버 조회해서 타입 넣기
    }

    public OrderCancelReturnDetailResponseDto findCancelReturnDetail(String orderNo, List<Long> productIds, String membership, DateTimeHolder dateTimeHolder) {

//        Order order = validOrder(orderNo);
//        List<OrderDetail> orderDetails = validOrderDetails(orderNo, productIds);
        Order order = orderQueryRepository.findByOrderNo(orderNo);
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);

        return OrderCancelReturnDetailResponseDto.of(order, orderDetails, membership, dateTimeHolder); // TODO 맴버 조회해서 타입 넣기
    }

//    private Order validOrder(String orderNo) {
//        Order order = orderQueryRepository.findByOrderNo(orderNo);
//        if (order == null) {
//            throw new EntityNotFoundException("조회된 주문이 없습니다.");
//        } return order;
//    }

//    private List<OrderDetail> validOrderDetails(String orderNo, List<Long> productIds) {
//        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);
//        if (orderDetails.isEmpty()) {
//            throw new EntityNotFoundException("조회된 주문 상세가 없습니다.");
//        }
//        return orderDetails;
//    }

    //    // TODO 객체로 따로 빼야함(임시로 사용)
//    class InnerService {
//        public Integer confirmCancelReturn(Long orderId, String reason) {
//            Order order = orderQueryRepository.findById(orderId)
//                    .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 없습니다."));
//
//            Integer cancelAmount = order.changeDetailsReasonAndStatus(reason, ORDER_CANCEL.getCode());
//
//            order.returnCoupon();
//
//            return cancelAmount;
//        }
//    }

}
