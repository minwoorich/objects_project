package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.dto.CreateOrderDto;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.payment.domain.*;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public void create(CreateOrderDto createOrderDto) {

        // 1. ProdOrder 생성
        ProdOrder prodOrder = createProdOrder(createOrderDto);

        // 2. ProdOrderDetail 생성
        List<ProdOrderDetail> prodOrderDetails = createProdOrderDetail(createOrderDto);

        // 3. Payment 생성
        Payment payment = createPayment(createOrderDto);

        // 4. ProdOrder - ProdOrderDetail 연관관계 매핑
        for (ProdOrderDetail orderDetail : prodOrderDetails) {
            prodOrder.addOrderDetail(orderDetail);
        }

        // 4.1 ProdOrder - Payment 연관관계 매핑
        payment.linkProdOrder(prodOrder);

        // 5. 영속성 저장
        orderDetailRepository.saveAll(prodOrderDetails);
        paymentRepository.save(payment);
    }

    private ProdOrder createProdOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        Long totalUsedCouponPrice = geTotalCouponPrice(createOrderDto);

        return ProdOrder.create(member, address, orderName, orderNo, totalOrderPrice, totalUsedCouponPrice);
    }

    private Long geTotalCouponPrice(CreateOrderDto createOrderDto) {

        List<Coupon> coupons = couponRepository.findAllByIds(createOrderDto.getProductValues().stream().map(ProductValue::getCouponId).filter(Objects::nonNull).collect(Collectors.toList()));

        return coupons.stream().mapToLong(Coupon::getPrice).sum();
    }

    private List<ProdOrderDetail> createProdOrderDetail(CreateOrderDto createOrderDto) {

        List<ProdOrderDetail> prodOrderDetails = new ArrayList<>();

        for (ProductValue productValue : createOrderDto.getProductValues()) {

            Product product = productRepository.findById(productValue.getProductId());
            Coupon coupon = couponRepository.findById(productValue.getCouponId());
            Long quantity = productValue.getQuantity();
            Long price = product.getPrice();

            // ProdOrderDetail 엔티티 생성
            ProdOrderDetail prodOrderDetail = ProdOrderDetail.create(product, coupon, quantity, price, StatusCodeType.ORDER_INIT.toString());

            // prodOrderDetails 에 추가
            prodOrderDetails.add(prodOrderDetail);
        }

        return prodOrderDetails;
    }

    private Payment createPayment(CreateOrderDto createOrderDto) {

        String orderNo = createOrderDto.getOrderNo();
        String paymentType = createOrderDto.getPaymentType();
        String paymentMethod = createOrderDto.getPaymentMethod();
        String paymentKey = createOrderDto.getPaymentKey();
        String paymentStatus = createOrderDto.getPaymentStatus();
        String refundStatus = createOrderDto.getRefundStatus();
        PaymentCancel paymentCancel = createOrderDto.getPaymentCancel();
        Card card = createOrderDto.getCard();
        VirtualAccount virtual = createOrderDto.getVirtual();
        Transfer transfer = createOrderDto.getTransfer();

        return Payment.create(orderNo, paymentType, paymentMethod, paymentKey, paymentStatus, refundStatus,  paymentCancel, card, virtual, transfer);
    }



}
