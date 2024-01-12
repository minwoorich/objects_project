package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.domain.StatusCodeType;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import com.objects.marketbridge.global.utils.GroupingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void create(CreateProdOrderDto prodOrderDto, List<CreateProdOrderDetailDto> prodOrderDetailDtos) {

        // 1. ProdOrder, ProdOrderDetail 엔티티 생성
        ProdOrder prodOrder = createProdOrder(prodOrderDto);
        orderRepository.save(prodOrder);
        List<ProdOrderDetail> prodOrderDetails = createProdOrderDetails(prodOrderDetailDtos);

        // 2. 양방향관계이므로 양쪽 다 서로의 값을 집어넣어줘야함
        prodOrderDetails.forEach(prodOrder::addOrderDetail);
        prodOrderDetails.forEach(p -> p.setOrder(prodOrder));

        // 3.주문 엔티티 저장
        orderDetailRepository.saveAll(prodOrderDetails);

        // 4.결제 엔티티 저장
        Member member = memberRepository.findById(prodOrderDto.getMemberId()).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));

        String memberName = member.getName();
        String orderName = prodOrder.getOrderName();
        Long totalOrderPrice = prodOrder.getTotalPrice();
        String orderNo = prodOrderDto.getOrderNo();
        String paymentMethod = prodOrderDto.getPaymentMethod();
        String phoneNo = member.getPhoneNo();

        // TODO : payment 엔티티에 주문생성 초기에 들어가야할 데이터 생각
        // TODO : Response 에 들어갈것 생각
        // TODO : payment 엔티티 저장하기 구현
//        Payment payment =  Payment.create();



    }

    private ProdOrder createProdOrder(CreateProdOrderDto dto) {

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(IllegalArgumentException::new);
        Address address = addressRepository.findById(dto.getAddressId());
        Long totalOrderPrice = dto.getTotalOrderPrice();
        String orderName = dto.getOrderName();
        String orderNo = dto.getOrderNo();

        return ProdOrder.create(member, address,orderName, orderNo, totalOrderPrice);
    }

    private List<ProdOrderDetail> createProdOrderDetails(List<CreateProdOrderDetailDto> dtos) {

        // request 에서 Product 를 추출하여 Map 으로 그룹핑
        Map<Long, Product> productMap = GroupingHelper.groupingByKey(getAllProducts(dtos), Product::getId);

        // request 에서 Coupon 을 추출하여 Map 으로 그룹핑
        Map<Long, Coupon> couponMap = GroupingHelper.groupingByKey(getAllCoupons(dtos), Coupon::getId);

        return dtos.stream().map(d ->
                ProdOrderDetail.create(
                        productMap.get(d.getProductId()),
                        couponMap.get(d.getUsedCouponId()),
                        d.getQuantity(),
                        d.getUnitOrderPrice(),
                        StatusCodeType.ORDER_INIT.getCode()
                )
        ).toList();
    }

    private List<Coupon> getAllCoupons(List<CreateProdOrderDetailDto> dtos) {
        return couponRepository.findAllByIds(extractCouponIds(dtos));
    }

    private List<Product> getAllProducts(List<CreateProdOrderDetailDto> dtos) {
        return productRepository.findAllById(extractProductIds(dtos));
    }

    private static List<Long> extractCouponIds(List<CreateProdOrderDetailDto> dtos) {
        return dtos.stream()
                .map(CreateProdOrderDetailDto::getUsedCouponId)
                .toList();
    }

    private static List<Long> extractProductIds(List<CreateProdOrderDetailDto> dtos) {
        return dtos.stream()
                .map(CreateProdOrderDetailDto::getProductId)
                .toList();
    }
}
