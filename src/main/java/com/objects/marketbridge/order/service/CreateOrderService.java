package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.MemberCoupon;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.*;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import com.objects.marketbridge.order.service.port.AddressRepository;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CreateOrderService {

    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final CalcTotalDiscountService calcTotalDiscountService;

    @Transactional
    public void create(CreateOrderDto createOrderDto) {

        // 1. Order 생성
        Order order = orderCommendRepository.save(createOrder(createOrderDto));

        // 2. OrderDetail 생성 (연관관계 매핑 여기서 해결)
        orderDetailCommendRepository.saveAll(createOrderDetails(createOrderDto.getProductValues(), order));

        // 3. 총 할인 금액(totalDiscount) , 실제 결제 금액(realPrice) 저장
        order.calcTotalDiscount(calcTotalDiscountService);

        // 4. MemberCoupon 의 isUsed 변경, 사용날짜 저장
        order.useCoupon(LocalDateTime.now());

        // 5. Product 의 stock 감소
        order.stockDecrease();
    }

    private Order createOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId());
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        String tid = createOrderDto.getTid();

        return Order.create(member, address, orderName, orderNo, totalOrderPrice, tid);
    }

    private List<OrderDetail> createOrderDetails(List<ProductValue> productValues, Order order) {

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (ProductValue productValue : productValues) {

            Product product = productRepository.findById(productValue.getProductId());
            // 쿠폰이 적용안된 product 가 존재할 경우 그냥 null 저장
            // TODO : coupon가져올때 memberCoupon 도 가져오게끔 수정해야함
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
