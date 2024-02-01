package com.objects.marketbridge.mock;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.OrderCancelReturnController;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.RefundService;
import com.objects.marketbridge.order.service.port.*;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class TestContainer {

    public final OrderCancelReturnController orderCancelReturnController;

    public final OrderCancelReturnService orderCancelReturnService;
    public final RefundService refundService;

    public final OrderQueryRepository orderQueryRepository;
    public final OrderCommendRepository orderCommendRepository;
    public final OrderDetailQueryRepository orderDetailQueryRepository;
    public final OrderDetailCommendRepository orderDetailCommendRepository;
    public final OrderDtoRepository orderDtoRepository;
    public final ProductRepository productRepository;
    public final CouponRepository couponRepository;
    public final MemberCouponRepository memberCouponRepository;
    public final MemberRepository memberRepository;

    @Builder
    public TestContainer(DateTimeHolder dateTimeHolder) {
        // Repository
        this.orderQueryRepository = new FakeOrderQueryRepository();
        this.orderCommendRepository = new FakeOrderCommendRepository();
        this.orderDetailQueryRepository = new FakeOrderDetailQueryRepository();
        this.orderDetailCommendRepository = new FakeOrderDetailCommendRepository();
        this.orderDtoRepository = new FakeOrderDtoRepository();
        this.productRepository = new FakeProductRepository();
        this.couponRepository = new FakeCouponRepository();
        this.memberCouponRepository = new FakeMemberCouponRepository();
        this.memberRepository = new FakeMemberRepository();

        // Service
        this.refundService = new FakeRefundService(dateTimeHolder);
        this.orderCancelReturnService = OrderCancelReturnService.builder()
                .dateTimeHolder(dateTimeHolder)
                .orderDetailQueryRepository(this.orderDetailQueryRepository)
                .orderQueryRepository(this.orderQueryRepository)
                .productRepository(this.productRepository)
                .refundService(this.refundService)
                .orderCommendRepository(this.orderCommendRepository)
                .orderDetailCommendRepository(this.orderDetailCommendRepository)
                .build();

        // Controller
        this.orderCancelReturnController = OrderCancelReturnController.builder()
                .dateTimeHolder(dateTimeHolder)
                .orderCancelReturnService(orderCancelReturnService)
                .orderDtoRepository(orderDtoRepository)
                .build();

    }
}
