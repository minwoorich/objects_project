package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static org.springframework.http.HttpStatus.*;


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
    public ConfirmCancelReturnDto.Response confirmCancelReturn(ConfirmCancelReturnDto.Request request) {
        // TODO Order fetchJoin
//        List<OrderDetail> orderDetails = valifyOrderDetatils(createOrderDetailIdsBy(request));
//        Map<Long, OrderDetail> orderDetailMap = createOrderDetailMapBy(orderDetails);
//        Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfoMap = createOrderDetailInfoMapBy(request);
//
//        Long totalPrice = getTotalPrice(orderDetails);
//        Integer cancelAmount = ChangeOrderDetailStatusAndGetCancelAmount(request, orderDetailMap, orderDetailInfoMap);
//        // TODO ERROR 발생시 대처
//        RefundDto refundDto = vailfyRefundDto(orderDetails, cancelAmount);
//
//        orderDetails.forEach(orderDetail -> orderDetail.changeMemberCouponInfo(null));
//
//        // TODO 판매자 계좌의 금액 감소(동시성 고려)
//
//        ServiceDto serviceDto = ServiceDto.of(totalPrice, orderDetails, refundDto, orderDetailMap, orderDetailInfoMap);
//
//        return ConfirmCancelReturnDto.Response.of(serviceDto, dateTimeHolder);
        return null;
    }

    public RequestCancelDto.Response findCancelInfo(Long orderDetailId, Long numberOfCancellation, String membership) {
        // TODO fetchJoin으로 변경 (Product 까지)
        OrderDetail orderDetail = valifyCancelOrderDetail(orderDetailId);

        return RequestCancelDto.Response.of(orderDetail, numberOfCancellation, membership);
    }

    public RequestReturnDto.Response findReturnInfo(Long orderDetailId, Long numberOfReturns, String membership) {
        // TODO fetchJoin으로 변경
        OrderDetail orderDetail = valifyReturnOrderDetail(orderDetailId);

        return RequestReturnDto.Response.of(orderDetail, numberOfReturns, membership);
    }

    public GetCancelDetailDto.Response findCancelDetail(Long orderDetailId, String membership) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);

        return GetCancelDetailDto.Response.of(orderDetail, membership, dateTimeHolder);
    }

    public GetCancelDetailDto.Response findReturnDetail(Long orderDetailId, String membership) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);

        return GetCancelDetailDto.Response.of(orderDetail, membership, dateTimeHolder);
    }

    private List<Long> createOrderDetailIdsBy(ConfirmCancelReturnDto.Request request) {
        return request.getOrderDetailInfos().stream()
                .map(ConfirmCancelReturnDto.OrderDetailInfo::getOrderDetailId)
                .toList();
    }

    private Map<Long, OrderDetail> createOrderDetailMapBy(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .collect(Collectors.toMap(OrderDetail::getId, Function.identity()));
    }

    private Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> createOrderDetailInfoMapBy(ConfirmCancelReturnDto.Request request) {
        return request.getOrderDetailInfos().stream()
                .collect(Collectors.toMap(ConfirmCancelReturnDto.OrderDetailInfo::getOrderDetailId, Function.identity()));
    }

    private Long getTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream().mapToLong(OrderDetail::totalAmount).sum();
    }

    private Integer ChangeOrderDetailStatusAndGetCancelAmount(ConfirmCancelReturnDto.Request request, Map<Long, OrderDetail> orderDetailMap, Map<Long, ConfirmCancelReturnDto.OrderDetailInfo> requestMap) {
        return orderDetailMap.keySet().stream().mapToInt(key -> orderDetailMap.get(key).changeReasonAndStatus(
                        request.getCancelReason(),
                        ORDER_CANCEL.getCode(),
                        requestMap.get(key).getNumberOfCancellation()
                )
        ).sum();
    }

    private RefundDto vailfyRefundDto(List<OrderDetail> orderDetails, Integer cancelAmount) {
        try {
            return refundClient.refund(getTid(orderDetails), cancelAmount);
        } catch (Exception e) {
            log.info("error : ", e);
            throw e;
        }
    }

    private String getTid(List<OrderDetail> orderDetails) {
        return orderDetails.get(0).getOrder().getTid();
    }

    private OrderDetail valifyCancelOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);
        if(cancelImpossible(orderDetail)) {
            throw CustomLogicException.builder()
                    .httpStatus(BAD_REQUEST)
                    .message("취소가 불가능한 상품입니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(NON_CANCELLABLE_PRODUCT)
                    .build();
        }
        return orderDetail;
    }

    private OrderDetail valifyReturnOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailQueryRepository.findById(orderDetailId);
        if(returnImpossible(orderDetail)) {
            throw CustomLogicException.builder()
                    .httpStatus(BAD_REQUEST)
                    .message("반품이 불가능한 상품입니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(NON_RETURNABLE_PRODUCT)
                    .build();
        }
        return orderDetail;
    }

    private boolean cancelImpossible(OrderDetail orderDetail) {
        return Objects.equals(orderDetail.getStatusCode(), DELIVERY_COMPLETED.getCode());
    }

    private boolean returnImpossible(OrderDetail orderDetail) {
        return !Objects.equals(orderDetail.getStatusCode(), DELIVERY_COMPLETED.getCode());
    }

    private List<OrderDetail> valifyOrderDetails(List<Long> orderDetailIds) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByIdIn(orderDetailIds);
        if (orderDetails.isEmpty()) {
            throw CustomLogicException.builder()
                    .httpStatus(NOT_FOUND)
                    .message("주문 상세 정보 리스트를 찾을 수 없습니다.")
                    .timestamp(LocalDateTime.now())
                    .errorCode(ORDERDETAIL_NOT_FOUND)
                    .build();
        }
        return orderDetails;
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
