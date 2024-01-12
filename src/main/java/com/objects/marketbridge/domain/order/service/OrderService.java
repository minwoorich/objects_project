package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
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

    @Transactional
    public void create(String userEmail, CreateOrderRequest request) {

        // request 에서 Product 를 추출하여 Map 으로 그룹핑
        Map<Long, Product> productMap = GroupingHelper.groupingByKey(getAllProducts(request), Product::getId);

        // request 에서 Coupon 을 추출하여 Map 으로 그룹핑
        Map<Long, Coupon> couponMap = GroupingHelper.groupingByKey(getAllCoupons(request), Coupon::getId);

        // request 에서 상품에 대한
        List<ProductInfoDto> productInfoDtos = request.getProductInfos();

        List<ProdOrderDetail> orderDetails =
                createOrderDetails(productInfoDtos, productMap, couponMap);

        //TODO : 1) ProdOrder 엔티티 생성, 2) 엔티티 저장
    }

    private List<ProdOrderDetail> createOrderDetails(List<ProductInfoDto> productInfoDtos, Map<Long, Product> productMap, Map<Long, Coupon> couponMap) {
        return productInfoDtos.stream().map(info ->
                ProdOrderDetail.create(
                        productMap.get(info.getProductId()),
                        couponMap.get(info.getUsedCouponId()),
                        info.getQuantity(),
                        info.getUnitOrderPrice()
                )
        ).toList();
    }

    private List<Coupon> getAllCoupons(CreateOrderRequest request) {
        return couponRepository.findAllByIds(extractCouponIds(request));
    }

    private List<Product> getAllProducts(CreateOrderRequest request) {
        return productRepository.findAllById(extractProductIds(request));
    }

    private static List<Long> extractCouponIds(CreateOrderRequest request) {
        return request.getProductInfos().stream()
                .map(ProductInfoDto::getUsedCouponId)
                .toList();
    }

    private static List<Long> extractProductIds(CreateOrderRequest request) {
        return request.getProductInfos().stream()
                .map(ProductInfoDto::getProductId)
                .toList();
    }
}
