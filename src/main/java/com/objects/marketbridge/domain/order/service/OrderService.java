package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.domain.ProdOrder;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.dto.ProductInfoDto;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
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

    @Transactional
    public void create(CreateProdOrderDto prodOrderDto, List<CreateProdOrderDetailDto> prodOrderDetailDtos) {
        ProdOrder prodOrder = createProdOrder(prodOrderDto);
        List<ProdOrderDetail> prodOrderDetails = createProdOrderDetails(prodOrderDetailDtos);

        //TODO : 1) ProdOrder 엔티티 생성, 2) 엔티티 저장

    }

    private ProdOrder createProdOrder(CreateProdOrderDto dto) {

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(IllegalArgumentException::new);
        Address address = addressRepository.findById(dto.getAddressId());
        Long totalOrderPrice = dto.getTotalOrderPrice();

        return ProdOrder.create(member, address, totalOrderPrice);
    }

    private List<ProdOrderDetail> createProdOrderDetails(List<CreateProdOrderDetailDto> dtos) {
        // request 에서 Product 를 추출하여 Map 으로 그룹핑
        List<Product> products = getAllProducts(dtos);
        Map<Long, Product> productMap = GroupingHelper.groupingByKey(products, Product::getId);

        // request 에서 Coupon 을 추출하여 Map 으로 그룹핑
        List<Coupon> coupons = getAllCoupons(dtos);
        Map<Long, Coupon> couponMap = GroupingHelper.groupingByKey(coupons, Coupon::getId);

        return dtos.stream().map(d ->
                ProdOrderDetail.create(
                        productMap.get(d.getProductId()),
                        couponMap.get(d.getUsedCouponId()),
                        d.getQuantity(),
                        d.getUnitOrderPrice()
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
