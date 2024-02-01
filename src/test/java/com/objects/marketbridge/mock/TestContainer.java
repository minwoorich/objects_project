package com.objects.marketbridge.mock;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.RefundService;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.product.infra.CouponRepository;
import com.objects.marketbridge.product.infra.MemberCouponRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.Builder;

public class TestContainer {

    public final OrderCancelReturnService orderCancelReturnService;
    public final RefundService refundService;

    public final OrderQueryRepository orderQueryRepository;
    public final OrderCommendRepository orderCommendRepository;
    public final OrderDetailQueryRepository orderDetailQueryRepository;
    public final OrderDetailCommendRepository orderDetailCommendRepository;
    public final ProductRepository productRepository;
    public final CouponRepository couponRepository;
    public final MemberCouponRepository memberCouponRepository;

    @Builder
    public TestContainer(DateTimeHolder dateTimeHolder) {
        this.orderQueryRepository = new FakeOrderQueryRepository();
        this.orderCommendRepository = new FakeOrderCommendRepository();
        this.orderDetailQueryRepository = new FakeOrderDetailQueryRepository();
        this.orderDetailCommendRepository = new FakeOrderDetailCommendRepository();
        this.productRepository = new FakeProductRepository();
        this.couponRepository = new FakeCouponRepository();
        this.memberCouponRepository = new FakeMemberCouponRepository();

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

    }
}
