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
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.global.utils.GroupingHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final TossPaymentConfig paymentConfig;


    @Transactional
    public CreateOrderResponse create(CreateOrderDto createOrderDto) {

        // 1. ProdOrder, ProdOrderDetail 엔티티 생성
        ProdOrder prodOrder = createProdOrder(createOrderDto);

        List<ProdOrderDetail> prodOrderDetails = createProdOrderDetail(createOrderDto);
        for (ProdOrderDetail orderDetail : prodOrderDetails) {
            prodOrder.addOrderDetail(orderDetail);
        }

        orderDetailRepository.saveAll(prodOrderDetails);



//        return CreateOrderResponse.from(prodOrderDto, email, successUrl, failUrl);
        return null;
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

    private ProdOrder createProdOrder(CreateOrderDto createOrderDto) {

        Member member = memberRepository.findById(createOrderDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findById(createOrderDto.getAddressId());
        String orderName = createOrderDto.getOrderName();
        String orderNo = createOrderDto.getOrderNo();
        Long totalOrderPrice = createOrderDto.getTotalOrderPrice();
        List<Coupon> coupons = getCoupons(createOrderDto);
        Long totalUsedCouponPrice = coupons.stream().mapToLong(Coupon::getPrice).sum();

        return ProdOrder.create(member, address, orderName, orderNo, totalOrderPrice, totalUsedCouponPrice);
    }

    private List<Coupon> getCoupons(CreateOrderDto createOrderDto) {
        return couponRepository.findAllByIds(createOrderDto.getProductValues().stream().map(ProductValue::getCouponId).filter(Objects::nonNull).collect(Collectors.toList()));
    }



}
