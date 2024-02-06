package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.*;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.payment.service.port.RefundClient;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Builder
@Slf4j
public class OrderCancelReturnService {

    private final DateTimeHolder dateTimeHolder;

    private final RefundClient refundClient;

    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final ProductRepository productRepository;

    // TODO 트랜잭션 위치 고려해야함
    @Transactional
    public ConfirmCancelReturnDto.Response confirmCancelReturn(ConfirmCancelReturnDto.Request request, DateTimeHolder dateTimeHolder) {
        // TODO Order fetchJoin
        List<OrderDetail> orderDetails = vaildOrderDetail(createOrderDetailIdsBy(request));
        Map<Long, OrderDetail> orderDetailMap = createOrderDetailMapBy(orderDetails);
        Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfoMap = createOrderDetailInfoMapBy(request);

        Long totalPrice = getTotalPrice(orderDetails);
        Integer cancelAmount = ChangeOrderDetailStatusAndGetCancelAmount(request, orderDetailMap, orderDetailInfoMap);
        // TODO ERROR 발생시 대처
        RefundDto refundDto = vaildRefundDto(orderDetails, cancelAmount);

        orderDetails.forEach(orderDetail -> orderDetail.changeMemberCouponInfo(null));

        // TODO 판매자 계좌의 금액 감소(동시성 고려)

        ServiceDto serviceDto = ServiceDto.of(totalPrice, orderDetails, refundDto, orderDetailMap, orderDetailInfoMap);

        return ConfirmCancelReturnDto.Response.of(serviceDto, dateTimeHolder);
    }

    private Long getTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream().mapToLong(OrderDetail::totalAmount).sum();
    }

    public RequestCancelDto.Response findCancelInfo(String orderNo, List<Long> productIds, String membership) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);

        return RequestCancelDto.Response.of(orderDetails, membership); // TODO 맴버 조회해서 타입 넣기
    }

    public RequestReturnDto.Response findReturnInfo(String orderNo, List<Long> productIds, String membership) {
        List<OrderDetail> orderDetails = validOrderDetails(orderNo, productIds);

        return RequestReturnDto.Response.of(orderDetails, membership); // TODO 맴버 조회해서 타입 넣기
    }

    public GetCancelReturnDetailDto.Response findCancelReturnDetail(String orderNo, List<Long> productIds, String membership, DateTimeHolder dateTimeHolder) {
        Order order = validOrder(orderNo);
        List<OrderDetail> orderDetails = validOrderDetails(orderNo, productIds);

        return GetCancelReturnDetailDto.Response.of(order, orderDetails, membership, dateTimeHolder); // TODO 맴버 조회해서 타입 넣기
    }


    private List<Long> createOrderDetailIdsBy(ConfirmCancelReturnDto.Request request) {
        return request.getOrderDetailInfos().stream()
                .map(ConfirmCancelReturnDto.OrderDetailInfo::getOrderDetailId)
                .toList();
    }

    private List<OrderDetail> vaildOrderDetail(List<Long> orderDetailIds) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByIdIn(orderDetailIds);
        if (orderDetails.isEmpty()) {
            throw CustomLogicException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("주문 상세 정보를 찾을 수 없습니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(ORDERDETAIL_NOT_FOUND)
                    .build();
        }
        return orderDetails;
    }

    private Map<Long, OrderDetail> createOrderDetailMapBy(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .collect(Collectors.toMap(OrderDetail::getId, Function.identity()));
    }

    private Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> createOrderDetailInfoMapBy(ConfirmCancelReturnDto.Request request) {
        return request.getOrderDetailInfos().stream()
                .collect(Collectors.toMap(ConfirmCancelReturnDto.OrderDetailInfo::getOrderDetailId, Function.identity()));
    }

    private Integer ChangeOrderDetailStatusAndGetCancelAmount(ConfirmCancelReturnDto.Request request, Map<Long, OrderDetail> orderDetailMap, Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> requestMap) {
        return orderDetailMap.keySet().stream().mapToInt(key -> orderDetailMap.get(key).changeReasonAndStatus(
                        request.getCancelReason(),
                        ORDER_CANCEL.getCode(),
                        requestMap.get(key).getNumberOfCancellation()
                )
        ).sum();
    }

    private String getTid(List<OrderDetail> orderDetails) {
        return orderDetails.get(0).getOrder().getTid();
    }

    private List<OrderDetail> validOrderDetails(String orderNo, List<Long> productIds) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);
        if (orderDetails.isEmpty()) {
            throw CustomLogicException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("주문 상세 정보 리스트를 찾을 수 없습니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(ORDERDETAIL_NOT_FOUND)
                    .build();
        }
        return orderDetails;
    }

    private RefundDto vaildRefundDto(List<OrderDetail> orderDetails, Integer cancelAmount) {
        try {
            return refundClient.refund(getTid(orderDetails), cancelAmount);
        } catch (Exception e) {
            log.info("error : ", e);
            throw e;
        }
    }

    private Order validOrder(String orderNo) {
        Order order = orderQueryRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw CustomLogicException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("주문 정보를 찾을 수 없습니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(ORDER_NOT_FOUND)
                    .build();
        }
        return order;
    }

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
