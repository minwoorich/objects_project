package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.order.controller.response.OrderCancelResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnListResponse;
import com.objects.marketbridge.domain.order.controller.response.OrderCancelReturnResponse;
import com.objects.marketbridge.domain.order.dto.OrderCancelServiceDto;
import com.objects.marketbridge.domain.order.dto.OrderReturnResponse;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.service.RefundService;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.error.ErrorCode;
import com.objects.marketbridge.model.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.objects.marketbridge.domain.order.entity.StatusCodeType.*;

@Service
@RequiredArgsConstructor
public class OrderCancelReturnService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    private final RefundService refundService;

    // TODO 트랜잭션 위치 고려해야함
    @Transactional
    public OrderCancelReturnResponse cancelReturnOrder(OrderCancelServiceDto orderCancelServiceDto, LocalDateTime cancelDateTime) {
        InnerService innerService = new InnerService();

        Order order = innerService.cancelReturn(
                orderCancelServiceDto.getOrderId(),
                orderCancelServiceDto.getCancelReason(),
                cancelDateTime
        );

        Payment payment = validPayment(orderCancelServiceDto.getOrderId());

        RefundDto refundDto = refundService.refund(
                payment,
                orderCancelServiceDto.getCancelReason(),
                order.getRealPrice()
        );

        return OrderCancelReturnResponse.of(order, refundDto);
    }

    @Transactional(readOnly = true)
    public OrderCancelResponse requestCancel(Long orderId, List<Long> productIds) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("조회한 주문이 없습니다."));
        List<Product> products = validProducts(productIds);

        List<OrderDetail> orderDetails = orderDetailRepository.findByProdOrder_IdAndProductIn(orderId, products);

        return OrderCancelResponse.of(orderDetails, order);
    }

    @Transactional(readOnly = true)
    public OrderReturnResponse requestReturn(Long orderId, List<Long> productIds) {
        List<Product> products = validProducts(productIds);
        List<OrderDetail> orderDetails = orderDetailRepository.findByProdOrder_IdAndProductIn(orderId, products);

        return OrderReturnResponse.of(orderDetails);
    }

    public OrderCancelReturnListResponse findCancelReturnList(Long memberId, Pageable pageable) {
        List<Order> orders = orderRepository.findDistinctWithDetailsByMemberId(memberId);
        return null;
    }

    // TODO 객체로 따로 빼야함(임시로 사용)
    class InnerService {
        public Order cancelReturn(Long orderId, String reason, LocalDateTime cancelDateTime) {
            Order order = orderRepository.findOrderWithDetailsAndProduct(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 주문이 없습니다."));

            order.cancelReturn(reason, ORDER_CANCEL.getCode(), cancelDateTime);

            order.returnCoupon();

            return order;
        }


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

}
