package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.domain.Coupon;
import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.mock.BaseFakeOrderRepository;
import com.objects.marketbridge.mock.TestContainer;
import com.objects.marketbridge.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.controller.dto.ConfirmCancelReturnHttp;
import com.objects.marketbridge.order.controller.dto.GetCancelReturnDetailHttp;
import com.objects.marketbridge.order.controller.dto.RequestReturnHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.infra.dtio.CancelReturnResponseDtio;
import com.objects.marketbridge.order.infra.dtio.DetailResponseDtio;
import com.objects.marketbridge.order.controller.dto.RequestCancelHttp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class OrderCancelReturnControllerTest {

    private OrderCancelReturnController orderCancelReturnController;

    @BeforeEach
    void beforeEach() {
        TestContainer testContainer = TestContainer.builder()
                .dateTimeHolder(TestDateTimeHolder.builder()
                        .build())
                .build();
        this.orderCancelReturnController = testContainer.orderCancelReturnController;

        Member member = Member.builder().build();

        Product product1 = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .price(1000L)
                .thumbImg("빵빵이썸네일")
                .stock(5L)
                .build();
        Product product2 = Product.builder()
                .name("옥지얌키링")
                .productNo("2")
                .price(2000L)
                .thumbImg("옥지얌썸네일")
                .stock(5L)
                .build();

        Coupon coupon1 = Coupon.builder()
                .name("빵빵이키링쿠폰")
                .product(product1)
                .price(1000L)
                .count(10L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .name("옥지얌키링쿠폰")
                .product(product2)
                .price(2000L)
                .count(10L)
                .build();

        Order order = Order.builder()
                .member(member)
                .orderNo("1")
                .tid("1")
                .totalDiscount(0L)
                .totalPrice(8000L)
                .realPrice(8000L)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 31, 3, 33);
        OrderDetail orderDetail1 = OrderDetail.builder()
                .cancelledAt(cancelledAt)
                .quantity(2L)
                .product(product1)
                .price(1000L)
                .coupon(coupon1)
                .order(order)
                .reason("단순변심")
                .orderNo("1")
//                .statusCode(ORDER_RECEIVED.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("1")
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .cancelledAt(cancelledAt)
                .quantity(3L)
                .product(product2)
                .price(2000L)
                .coupon(coupon2)
                .order(order)
                .reason("단순변심")
                .orderNo("1")
//                .statusCode(DELIVERY_ING.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("1")
                .build();

        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);

        testContainer.productRepository.save(product1);
        testContainer.productRepository.save(product2);
        testContainer.orderDetailCommendRepository.save(orderDetail1);
        testContainer.orderDetailCommendRepository.save(orderDetail2);
        testContainer.orderCommendRepository.save(order);
        testContainer.memberRepository.save(member);
    }

    @AfterEach
    void afterEach() {
        BaseFakeOrderDetailRepository.getInstance().clear();
        BaseFakeOrderRepository.getInstance().clear();
    }

    // TODO 취소시간 테스트 고려
    @Test
    @DisplayName("")
    public void cancelReturnOrder() {
        // given
        ConfirmCancelReturnHttp.Request request = ConfirmCancelReturnHttp.Request.builder()
                .cancelReason("단순변심")
                .orderNo("1")
                .build();

        // when
        ApiResponse<ConfirmCancelReturnHttp.Response> result = orderCancelReturnController.confirmCancelReturn(request);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
        assertThat(result.getData().getOrderId()).isEqualTo(1L);
        assertThat(result.getData().getOrderNumber()).isEqualTo("1");
        assertThat(result.getData().getTotalPrice()).isEqualTo(8000L);

        assertThat(result.getData().getCancelledItems().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getData().getCancelledItems().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getData().getCancelledItems().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getCancelledItems().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getCancelledItems().get(0).getQuantity()).isEqualTo(2L);

        assertThat(result.getData().getCancelledItems().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getData().getCancelledItems().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getData().getCancelledItems().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getData().getCancelledItems().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getData().getCancelledItems().get(1).getQuantity()).isEqualTo(3L);

        assertThat(result.getData().getRefundInfo().getRefundMethod()).isEqualTo("카드");
        assertThat(result.getData().getRefundInfo().getTotalRefundAmount()).isEqualTo(8000L);

    }

    @Test
    @DisplayName("")
    public void requestCancelOrder() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);

        // when
        ApiResponse<RequestCancelHttp.Response> result = orderCancelReturnController.requestCancel(orderNo, productIds);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
        assertThat(result.getData().getCancelRefundInfoResponse().getDeliveryFee()).isEqualTo(WOW.getDeliveryFee());
        assertThat(result.getData().getCancelRefundInfoResponse().getRefundFee()).isEqualTo(WOW.getRefundFee());
        assertThat(result.getData().getCancelRefundInfoResponse().getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getData().getCancelRefundInfoResponse().getTotalPrice()).isEqualTo(8000L);

        assertThat(result.getData().getProductResponses().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductResponses().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductResponses().get(0).getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductResponses().get(0).getPrice()).isEqualTo(1000L);

        assertThat(result.getData().getProductResponses().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getData().getProductResponses().get(1).getQuantity()).isEqualTo(3L);
        assertThat(result.getData().getProductResponses().get(1).getImage()).isEqualTo("옥지얌썸네일");
        assertThat(result.getData().getProductResponses().get(1).getPrice()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("")
    public void requestReturnOrder() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);

        // when
        ApiResponse<RequestReturnHttp.Response> result = orderCancelReturnController.requestReturn(orderNo, productIds);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
        assertThat(result.getData().getReturnRefundInfoResponse().getDeliveryFee()).isEqualTo(WOW.getDeliveryFee());
        assertThat(result.getData().getReturnRefundInfoResponse().getReturnFee()).isEqualTo(WOW.getReturnFee());
        assertThat(result.getData().getReturnRefundInfoResponse().getProductPrice()).isEqualTo(8000L);

        assertThat(result.getData().getProductResponses().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductResponses().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductResponses().get(0).getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductResponses().get(0).getPrice()).isEqualTo(1000L);

        assertThat(result.getData().getProductResponses().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getData().getProductResponses().get(1).getQuantity()).isEqualTo(3L);
        assertThat(result.getData().getProductResponses().get(1).getImage()).isEqualTo("옥지얌썸네일");
        assertThat(result.getData().getProductResponses().get(1).getPrice()).isEqualTo(2000L);
    }

    @Test
    @DisplayName("")
    public void getCancelReturnList() {
        // given
        Long memberId = 1L;
        Integer page = 0;
        Integer size = 5;

        // when
        ApiResponse<Page<CancelReturnResponseDtio>> result = orderCancelReturnController.getCancelReturnList(memberId, page, size);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
        assertThat(result.getData().getContent().size()).isEqualTo(1);
        assertThat(result.getData().getContent().get(0).getOrderNo()).isEqualTo("1");

        List<DetailResponseDtio> dtios = result.getData().getContent().get(0).getDetailResponseDtios();
        assertThat(dtios.get(0).getOrderNo()).isEqualTo("1");
        assertThat(dtios.get(0).getProductId()).isEqualTo(1L);
        assertThat(dtios.get(0).getProductNo()).isEqualTo("1");
        assertThat(dtios.get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(dtios.get(0).getPrice()).isEqualTo(1000L);
        assertThat(dtios.get(0).getQuantity()).isEqualTo(2L);
        assertThat(dtios.get(0).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());

        assertThat(dtios.get(1).getOrderNo()).isEqualTo("1");
        assertThat(dtios.get(1).getProductId()).isEqualTo(2L);
        assertThat(dtios.get(1).getProductNo()).isEqualTo("2");
        assertThat(dtios.get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(dtios.get(1).getPrice()).isEqualTo(2000L);
        assertThat(dtios.get(1).getQuantity()).isEqualTo(3L);
        assertThat(dtios.get(1).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("")
    public void getCancelReturnDetail() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);

        // when
        ApiResponse<GetCancelReturnDetailHttp.Response> result = orderCancelReturnController.getCancelReturnDetail(orderNo, productIds);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
//        assertThat(result.getData().getOrderDate()).isEqualTo()
//        assertThat(result.getData().getCancelDate()).isEqualTo()
        assertThat(result.getData().getOrderNo()).isEqualTo("1");
        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getData().getProductResponseList().size()).isEqualTo(2);
        assertThat(result.getData().getProductResponseList().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getData().getProductResponseList().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getData().getProductResponseList().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductResponseList().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getProductResponseList().get(0).getQuantity()).isEqualTo(2L);

        assertThat(result.getData().getProductResponseList().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getData().getProductResponseList().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getData().getProductResponseList().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getData().getProductResponseList().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getData().getProductResponseList().get(1).getQuantity()).isEqualTo(3L);


    }
}
