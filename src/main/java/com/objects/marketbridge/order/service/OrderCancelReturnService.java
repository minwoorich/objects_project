package com.objects.marketbridge.order.service;


import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.common.exception.error.CustomLogicException;
import com.objects.marketbridge.common.exception.error.ErrorCode;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.dto.*;
import com.objects.marketbridge.order.service.port.*;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import com.objects.marketbridge.payment.service.port.PaymentRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.objects.marketbridge.common.domain.Membership.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;


@Service
@RequiredArgsConstructor
public class OrderCancelReturnService {

    private final DateTimeHolder dateTimeHolder;

    private final RefundService refundService;

    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderDtoRepository orderDtoRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;


    // TODO 트랜잭션 위치 고려해야함
    @Transactional
    public CancelReturnResponseDto confirmCancelReturn(CancelRequestDto cancelRequestDto) {
        InnerService innerService = new InnerService();

        Order order = innerService.cancelReturn(
                cancelRequestDto.getOrderId(),
                cancelRequestDto.getCancelReason()
        );

        Payment payment = validPayment(cancelRequestDto.getOrderId());

        RefundDto refundDto = refundService.refund(
                payment,
                cancelRequestDto.getCancelReason(),
                order.getRealPrice()
        );

        return CancelReturnResponseDto.of(order, refundDto);
    }

    @Transactional(readOnly = true)
    public CancelResponseDto requestCancel(Long orderId, List<Long> productIds) {
        Order order = orderQueryRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("조회한 주문이 없습니다."));
        List<Product> products = validProducts(productIds);

        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrder_IdAndProductIn(orderId, products);

        return CancelResponseDto.of(orderDetails, WOW.getText()); // TODO 맴버 조회해서 타입 넣기
    }

    @Transactional(readOnly = true)
    public ReturnResponseDto requestReturn(Long orderId, List<Long> productIds) {
        List<Product> products = validProducts(productIds);
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrder_IdAndProductIn(orderId, products);

        return ReturnResponseDto.of(orderDetails, WOW.getText()); // TODO 맴버 조회해서 타입 넣기
    }

    @Transactional(readOnly = true)
    public OrderCancelReturnDetailResponseDto findCancelReturnDetail(String orderNo, Long paymentId, List<Long> productIds) {
        Order order = validOrder(orderNo);
        List<OrderDetail> orderDetails = validOrderDetails(orderNo, productIds);
        Payment payment = vaildPayment(paymentId);

        return OrderCancelReturnDetailResponseDto.of(order, orderDetails, WOW.getText(), dateTimeHolder); // TODO 맴버 조회해서 타입 넣기
    }

    // TODO 객체로 따로 빼야함(임시로 사용)
    class InnerService {
        public Order cancelReturn(Long orderId, String reason) {
            Order order = orderQueryRepository.findByIdWithOrderDetailsAndProduct(orderId);

            order.cancelReturn(reason, ORDER_CANCEL.getCode());

            order.returnCoupon();

            return order;
        }
    }
    private List<OrderDetail> validOrderDetails(String orderNo, List<Long> productIds) {
        List<OrderDetail> orderDetails = orderDetailQueryRepository.findByOrderNoAndProduct_IdIn(orderNo, productIds);
        if (orderDetails.isEmpty()) {
            throw new EntityNotFoundException("조회된 주문 상세가 없습니다.");
        }
        return orderDetails;
    }

    private Payment vaildPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new EntityNotFoundException("조회된 결재가 없습니다.");
        }
        return payment;
    }

    private Payment validPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            throw new CustomLogicException(ErrorCode.PAYMENT_NOT_FOUND.getMessage());
        }
        return payment;
    }

    private List<Product> validProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products == null) {
            throw new NoSuchElementException("조회된 상품이 없습니다.");
        }
        return products;
    }

    private Order validOrder(String orderNo) {
        Order order = orderQueryRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new EntityNotFoundException("조회된 주문이 없습니다.");
        } return order;
    }

}
