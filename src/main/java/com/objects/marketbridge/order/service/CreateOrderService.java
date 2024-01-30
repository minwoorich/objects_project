package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.domain.*;
import com.objects.marketbridge.order.service.port.AddressRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CreateOrderService {

    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponUsageService couponUsageService;
    private final ProductStockService productStockService;

    @Transactional
    public void create(CreateOrderDto createOrderDto) {

        // 1. Order 생성
        Order order = orderCommendRepository.save(createOrder(createOrderDto));

        // 2. OrderDetail 생성 (연관관계 매핑 여기서 해결)
        List<OrderDetail> orderDetails = orderDetailCommendRepository.saveAll(createOrderDetails(createOrderDto.getProductValues(), order));

        // 3. Order 에 최종쿠폰사용 금액 집어넣기
        order.setTotalUsedCouponPrice(getTotalCouponPrice(orderDetails));

        // 4. MemberCoupon 의 isUsed 변경
        List<MemberCoupon> memberCoupons = getMemberCoupons(orderDetails, createOrderDto.getMemberId());
        couponUsageService.applyCouponUsage(memberCoupons, true, LocalDateTime.now());

        // 5. Product 의 stock 감소
        productStockService.decrease(orderDetails);
    }

    private List<MemberCoupon> getMemberCoupons(List<OrderDetail> orderDetails, Long memberId) {

        return orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .map(o ->
                        memberCouponRepository.findByMember_IdAndCoupon_Id(
                                memberId,
                                o.getCoupon().getId())
                ).collect(Collectors.toList());
    }
    private Long getTotalCouponPrice(List<OrderDetail> orderDetails) {

        return orderDetails.stream()
                .filter(o -> o.getCoupon() != null)
                .mapToLong(o -> o.getCoupon().getPrice())
                .sum();
    }

    private Order createOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId());
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        Long realOrderPrice = createOrderDto.getRealOrderPrice();
        String tid = createOrderDto.getTid();

        return Order.create(member, address, orderName, orderNo, totalOrderPrice, realOrderPrice, tid);
    }

    private List<OrderDetail> createOrderDetails(List<ProductValue> productValues, Order order) {

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (ProductValue productValue : productValues) {

            Product product = productRepository.findById(productValue.getProductId()).get();
            // 쿠폰이 적용안된 product 가 존재할 경우 그냥 null 저장
            Coupon coupon = (productValue.getCouponId() != null) ? couponRepository.findById(productValue.getCouponId()) : null ;
            String orderNo = order.getOrderNo();
            Long quantity = productValue.getQuantity();
            Long price = product.getPrice();
            String tid = order.getTid();

            // OrderDetail 엔티티 생성
            OrderDetail orderDetail =
                    OrderDetail.create(tid, order, product, orderNo, coupon, quantity, price, StatusCodeType.ORDER_INIT.getCode());

            // orderDetails 에 추가
            orderDetails.add(orderDetail);

            // 연관관계 매핑
            order.addOrderDetail(orderDetail);
        }

        return orderDetails;
    }
}
