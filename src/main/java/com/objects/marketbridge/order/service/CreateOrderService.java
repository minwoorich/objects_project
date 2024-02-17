package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.coupon.domain.MemberCoupon;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.ProductValue;
import com.objects.marketbridge.order.service.dto.CreateOrderDto;
import com.objects.marketbridge.order.service.port.AddressRepository;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.coupon.MemberCouponRepository;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_INIT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CreateOrderService {

    private final OrderDetailCommendRepository orderDetailCommendRepository;
    private final OrderCommendRepository orderCommendRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final AddressRepository addressRepository;
    private final DateTimeHolder dateTimeHolder;

    @Transactional
    public void create(CreateOrderDto createOrderDto) {

        // 1. Order 생성
        Order order = orderCommendRepository.save(createOrder(createOrderDto));

        // 2. OrderDetail 생성 (연관관계 매핑 여기서 해결)
        orderDetailCommendRepository.saveAll(createOrderDetails(createOrderDto.getProductValues(), order));

        // 3. MemberCoupon 의 isUsed 변경, 사용날짜 저장
        order.changeMemberCouponInfo(dateTimeHolder);

        // 4. Product 의 stock 감소
        order.stockDecrease();
    }

    private Order createOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId());
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        Long realOrderPrice = createOrderDto.getRealOrderPrice();
        Long totalDiscountPrice = createOrderDto.getTotalDiscountPrice();
        String tid = createOrderDto.getTid();

        return Order.create(member, address, orderName, orderNo, totalOrderPrice, realOrderPrice, totalDiscountPrice, tid);
    }

    private List<OrderDetail> createOrderDetails(List<ProductValue> productValues, Order order) {

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (ProductValue productValue : productValues) {

            Product product = productRepository.findById(productValue.getProductId());
            // 쿠폰이 적용안된 product 가 존재할 경우 그냥 null 저장
            MemberCoupon memberCoupon = (productValue.getCouponId() != null) ? memberCouponRepository.findByMemberIdAndCouponId(order.getMember().getId(), productValue.getCouponId()) : null;
            String orderNo = order.getOrderNo();
            Long quantity = productValue.getQuantity();
            String tid = order.getTid();
            Long price = product.getPrice();
            Long sellerId = productValue.getSellerId();

            // OrderDetail 엔티티 생성
            OrderDetail orderDetail =
                    OrderDetail.create(tid, order, product, orderNo, memberCoupon, price, quantity, sellerId, ORDER_INIT.getCode());

            // orderDetails 에 추가
            orderDetails.add(orderDetail);

            // 연관관계 매핑
            order.addOrderDetail(orderDetail);
        }

        return orderDetails;
    }
}
