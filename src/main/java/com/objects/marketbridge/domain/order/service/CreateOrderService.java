package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.coupon.repository.MemberCouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.model.*;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.domain.Transfer;
import com.objects.marketbridge.domain.payment.domain.VirtualAccount;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateOrderService {

    private final TossPaymentConfig tossPaymentConfig;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponUsageService couponUsageService;
    private final ProductStockService productStockService;

    @Transactional
    public CreateOrderResponse create(CreateOrderDto createOrderDto) {

        // 1. Order 생성
        Order order = createOrder(createOrderDto);
        orderRepository.save(order);

        // 2. OrderDetail 생성 (연관관계 매핑 여기서 해결)
        List<OrderDetail> orderDetails = createOrderDetail(createOrderDto.getProductValues(), order);
        orderDetailRepository.saveAll(orderDetails);

        // 3. Order 에 최종쿠폰사용 금액 집어넣기
        order.calculateTotalUsedCouponPrice(getTotalCouponPrice(orderDetails));

        // 4. MemberCoupon 의 isUsed 변경
        couponUsageService.applyCouponUsage(memberCouponRepository, orderDetails, createOrderDto.getMemberId());

        // 5. Product 의 stock 감소
        productStockService.decrease(orderDetails);

        return createOrderResponse(createOrderDto);
    }

    private Long getTotalCouponPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(OrderDetail::getCoupon)
                .filter(Objects::nonNull)
                .mapToLong(Coupon::getPrice)
                .sum();
    }

    private Order createOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        Long realOrderPrice = createOrderDto.getRealOrderPrice();

        return Order.create(member, address, orderName, orderNo, totalOrderPrice, realOrderPrice);
    }

    private List<OrderDetail> createOrderDetail(List<ProductValue> productValues, Order order) {

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (ProductValue productValue : productValues) {

            Product product = productRepository.findById(productValue.getProductId());
            // 쿠폰이 적용안된 product 가 존재할 경우 그냥 null 저장
            Coupon coupon = (productValue.getCouponId() != null) ? couponRepository.findById(productValue.getCouponId()) : null ;
            String orderNo = order.getOrderNo();
            Long quantity = productValue.getQuantity();
            Long price = product.getPrice();

            // OrderDetail 엔티티 생성
            OrderDetail orderDetail =
                    OrderDetail.create(order, product, orderNo, coupon, quantity, price, StatusCodeType.ORDER_INIT.getCode());

            // orderDetails 에 추가
            order.addOrderDetail(orderDetail);
        }

        return orderDetails;
    }

    private CreateOrderResponse createOrderResponse(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId()).orElseThrow(EntityNotFoundException::new);

        return createOrderDto.toResponse(
                member.getEmail(),
                tossPaymentConfig.getSuccessUrl(),
                tossPaymentConfig.getFailUrl());
    }
}
