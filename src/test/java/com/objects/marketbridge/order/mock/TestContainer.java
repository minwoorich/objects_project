package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.OrderCancelController;
import com.objects.marketbridge.order.controller.OrderCancelReturnController;
import com.objects.marketbridge.order.controller.OrderReturnController;
import com.objects.marketbridge.order.service.OrderCancelService;
import com.objects.marketbridge.order.service.OrderReturnService;
import com.objects.marketbridge.order.service.port.*;
import com.objects.marketbridge.payment.service.port.RefundClient;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.Builder;

public class TestContainer {

    public final OrderCancelReturnController orderCancelReturnController;
    public final OrderCancelController orderCancelController;
    public final OrderReturnController orderReturnController;

    public final OrderCancelService orderCancelService;
    public final OrderReturnService orderReturnService;

    public final OrderQueryRepository orderQueryRepository;
    public final OrderCommendRepository orderCommendRepository;
    public final OrderDetailQueryRepository orderDetailQueryRepository;
    public final OrderDetailCommendRepository orderDetailCommendRepository;
    public final OrderDtoRepository orderDtoRepository;
    public final OrderDetailDtoRepository orderDetailDtoRepository;
    public final ProductRepository productRepository;
    public final CouponRepository couponRepository;
    public final MemberCouponRepository memberCouponRepository;
    public final MemberRepository memberRepository;
    public final RefundClient refundClient;

    @Builder
    public TestContainer(DateTimeHolder dateTimeHolder) {
        // Repository
        this.orderQueryRepository = new FakeOrderQueryRepository();
        this.orderCommendRepository = new FakeOrderCommendRepository();
        this.orderDetailQueryRepository = new FakeOrderDetailQueryRepository();
        this.orderDetailCommendRepository = new FakeOrderDetailCommendRepository();
        this.orderDtoRepository = new FakeOrderDtoRepository();
        this.orderDetailDtoRepository = new FakeOrderDetailDtoRepository();
        this.productRepository = new FakeProductRepository();
        this.couponRepository = new FakeCouponRepository();
        this.memberCouponRepository = new FakeMemberCouponRepository();
        this.memberRepository = new FakeMemberRepository();
        this.refundClient = new FakeRefundClient(dateTimeHolder);

        // Service
        this.orderCancelService = OrderCancelService.builder()
                .orderDetailQueryRepository(this.orderDetailQueryRepository)
                .orderDetailCommendRepository(this.orderDetailCommendRepository)
                .refundClient(this.refundClient)
                .dateTimeHolder(dateTimeHolder)
                .build();
        this.orderReturnService = OrderReturnService.builder()
                .orderDetailQueryRepository(this.orderDetailQueryRepository)
                .orderDetailCommendRepository(this.orderDetailCommendRepository)
                .refundClient(this.refundClient)
                .dateTimeHolder(dateTimeHolder)
                .build();

        // Controller
        this.orderCancelReturnController = OrderCancelReturnController.builder()
                .orderDetailDtoRepository(this.orderDetailDtoRepository)
                .build();
        this.orderCancelController = OrderCancelController.builder()
                .orderCancelService(this.orderCancelService)
                .memberRepository(this.memberRepository)
                .dateTimeHolder(dateTimeHolder)
                .build();
        this.orderReturnController = OrderReturnController.builder()
                .orderReturnService(this.orderReturnService)
                .memberRepository(this.memberRepository)
                .dateTimeHolder(dateTimeHolder)
                .build();

    }
}
