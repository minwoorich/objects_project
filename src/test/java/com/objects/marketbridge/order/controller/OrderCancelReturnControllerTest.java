package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.*;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.order.mock.BaseFakeOrderRepository;
import com.objects.marketbridge.order.mock.TestContainer;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.member.domain.MembershipType.*;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.*;

public class OrderCancelReturnControllerTest {

    private TestContainer testContainer = TestContainer.builder().build();

//    @BeforeEach
    void beforeEach() {
        Member member1 = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();
        Member member2 = Member.builder()
                .membership(BASIC.getText())
                .build();

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
                .product(product1)
                .price(500L)
                .name("빵빵이키링 쿠폰")
                .build();
        Coupon coupon2 = Coupon.builder()
                .product(product2)
                .price(1000L)
                .name("옥지얌키링 쿠폰")
                .build();

        MemberCoupon memberCoupon1 = MemberCoupon.builder()
                .coupon(coupon1)
                .member(member1)
                .isUsed(true)
                .build();
        MemberCoupon memberCoupon2 = MemberCoupon.builder()
                .coupon(coupon2)
                .isUsed(true)
                .member(null)
                .build();

        Order order1 = Order.builder()
                .member(member1)
                .orderNo("1")
                .tid("1")
                .totalDiscount(0L)
                .totalPrice(30000L)
                .realPrice(30000L)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 31, 3, 33);
        OrderDetail orderDetail1 = OrderDetail.builder()
                .memberCoupon(memberCoupon1)
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product1)
                .price(1000L)
                .order(order1)
                .reason("단순변심")
                .orderNo("1")
//                .statusCode(ORDER_RECEIVED.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("1")
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product2)
                .price(2000L)
                .order(order1)
                .reason("단순변심")
                .orderNo("1")
//                .statusCode(DELIVERY_ING.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("1")
                .build();

        order1.addOrderDetail(orderDetail1);
        order1.addOrderDetail(orderDetail2);

        Product product3 = Product.builder()
                .name("빵빵이키링3")
                .productNo("3")
                .price(1000L)
                .thumbImg("빵빵이썸네일3")
                .stock(5L)
                .build();
        Product product4 = Product.builder()
                .name("옥지얌키링4")
                .productNo("4")
                .price(2000L)
                .thumbImg("옥지얌썸네일4")
                .stock(5L)
                .build();

        Coupon coupon3 = Coupon.builder()
                .product(product3)
                .price(500L)
                .name("빵빵이키링1 쿠폰")
                .build();
        Coupon coupon4 = Coupon.builder()
                .product(product4)
                .price(1000L)
                .name("옥지얌키링1 쿠폰")
                .build();

        MemberCoupon memberCoupon3 = MemberCoupon.builder()
                .coupon(coupon3)
                .member(member2)
                .isUsed(true)
                .build();
        MemberCoupon memberCoupon4 = MemberCoupon.builder()
                .coupon(coupon4)
                .isUsed(true)
                .member(member2)
                .build();

        Order order2 = Order.builder()
                .member(member2)
                .orderNo("2")
                .tid("2")
                .totalDiscount(0L)
                .totalPrice(30000L)
                .realPrice(30000L)
                .build();

        LocalDateTime cancelledAt2 = LocalDateTime.of(2024, 1, 31, 3, 33);
        OrderDetail orderDetail3 = OrderDetail.builder()
                .memberCoupon(memberCoupon3)
                .cancelledAt(cancelledAt2)
                .quantity(10L)
                .product(product3)
                .price(1000L)
                .order(order2)
                .reason("단순변심")
                .orderNo("2")
//                .statusCode(ORDER_RECEIVED.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("2")
                .build();
        OrderDetail orderDetail4 = OrderDetail.builder()
                .memberCoupon(memberCoupon4)
                .cancelledAt(cancelledAt2)
                .quantity(10L)
                .product(product4)
                .price(2000L)
                .order(order2)
                .reason("단순변심")
                .orderNo("2")
//                .statusCode(DELIVERY_ING.getCode())
                .statusCode(ORDER_CANCEL.getCode())
                .tid("2")
                .build();

        order2.addOrderDetail(orderDetail3);
        order2.addOrderDetail(orderDetail4);

        testContainer.productRepository.save(product1);
        testContainer.productRepository.save(product2);
        testContainer.productRepository.save(product3);
        testContainer.productRepository.save(product4);
        testContainer.orderDetailCommendRepository.save(orderDetail1);
        testContainer.orderDetailCommendRepository.save(orderDetail2);
        testContainer.orderDetailCommendRepository.save(orderDetail3);
        testContainer.orderDetailCommendRepository.save(orderDetail4);
        testContainer.orderCommendRepository.save(order1);
        testContainer.orderCommendRepository.save(order2);
        testContainer.memberRepository.save(member1);
        testContainer.memberRepository.save(member2);
    }

    @AfterEach
    void afterEach() {
        BaseFakeOrderDetailRepository.getInstance().clear();
        BaseFakeOrderRepository.getInstance().clear();
        testContainer.memberRepository.deleteAllInBatch();
        testContainer.productRepository.deleteAllInBatch();
    }

    // TODO 취소시간 테스트 고려 + 반품/취소 구별 필요
//    @Test
//    @DisplayName("취소/반품 확정")
//    public void confirmCancelReturn() {
//        // given
//        ConfirmCancelReturnHttp.OrderDetailInfo orderDetailInfo1 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
//                .orderDetailId(1L)
//                .numberOfCancellation(1L)
//                .build();
//        ConfirmCancelReturnHttp.OrderDetailInfo orderDetailInfo2 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
//                .orderDetailId(2L)
//                .numberOfCancellation(2L)
//                .build();
//        List<ConfirmCancelReturnHttp.OrderDetailInfo> orderDetailInfos = List.of(orderDetailInfo1, orderDetailInfo2);
//
//        ConfirmCancelReturnHttp.Request request = ConfirmCancelReturnHttp.Request.builder()
//                .orderDetailInfos(orderDetailInfos)
//                .cancelReason("단순변심")
//                .build();
//
//        // when
//        ApiResponse<ConfirmCancelReturnHttp.Response> result = orderCancelReturnController.confirmCancelReturn(request);
//
//        // then
//        assertThat(result.getCode()).isEqualTo(OK.value());
//        assertThat(result.getStatus()).isEqualTo(OK);
//        assertThat(result.getMessage()).isEqualTo(OK.name());
//        assertThat(result.getData().getOrderId()).isEqualTo(1L);
//        assertThat(result.getData().getOrderNo()).isEqualTo("1");
//        assertThat(result.getData().getTotalPrice()).isEqualTo(30000L);
//
//        assertThat(result.getData().getCancelledItems().get(0).getProductId()).isEqualTo(1L);
//        assertThat(result.getData().getCancelledItems().get(0).getProductNo()).isEqualTo("1");
//        assertThat(result.getData().getCancelledItems().get(0).getName()).isEqualTo("빵빵이키링");
//        assertThat(result.getData().getCancelledItems().get(0).getPrice()).isEqualTo(1000L);
//        assertThat(result.getData().getCancelledItems().get(0).getQuantity()).isEqualTo(1L);
//
//        assertThat(result.getData().getCancelledItems().get(1).getProductId()).isEqualTo(2L);
//        assertThat(result.getData().getCancelledItems().get(1).getProductNo()).isEqualTo("2");
//        assertThat(result.getData().getCancelledItems().get(1).getName()).isEqualTo("옥지얌키링");
//        assertThat(result.getData().getCancelledItems().get(1).getPrice()).isEqualTo(2000L);
//        assertThat(result.getData().getCancelledItems().get(1).getQuantity()).isEqualTo(2L);
//
//        assertThat(result.getData().getRefundInfo().getRefundMethod()).isEqualTo("카드");
//        assertThat(result.getData().getRefundInfo().getTotalRefundAmount()).isEqualTo(5000L);
//    }



    @Test
    @DisplayName("취소 요청 (WOW_NoCoupon)")
    public void requestCancel_WOW_And_NoCoupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestCancelHttp.Response> result = testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getCancelRefundInfo().getDeliveryFee()).isEqualTo(WOW.getDeliveryFee());
        assertThat(result.getData().getCancelRefundInfo().getRefundFee()).isEqualTo(WOW.getRefundFee());
        assertThat(result.getData().getCancelRefundInfo().getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getData().getCancelRefundInfo().getTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("취소 요청 (BASIC_NoCoupon)")
    public void requestCancel_BASIC_And_NoCoupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestCancelHttp.Response> result = testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getCancelRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getData().getCancelRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getData().getCancelRefundInfo().getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getData().getCancelRefundInfo().getTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("취소 요청 (WOW_Coupon)")
    public void requestCancel_WOW_And_Coupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .name("빵빵이키링 쿠폰")
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .member(member)
                .isUsed(true)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestCancelHttp.Response> result = testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getCancelRefundInfo().getDeliveryFee()).isEqualTo(WOW.getDeliveryFee());
        assertThat(result.getData().getCancelRefundInfo().getRefundFee()).isEqualTo(WOW.getRefundFee());
        assertThat(result.getData().getCancelRefundInfo().getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getData().getCancelRefundInfo().getTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("취소 요청 (BASIC_Coupon)")
    public void requestCancel_BASIC_And_Coupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .name("빵빵이키링 쿠폰")
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .member(member)
                .isUsed(true)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestCancelHttp.Response> result = testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getCancelRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getData().getCancelRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getData().getCancelRefundInfo().getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getData().getCancelRefundInfo().getTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("저장된 맴버가 없을 때 취소 요청을 하면 에러가 발생한다.")
    public void requestCancel_NoMember_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("저장된 주문 상세가 없을 때 취소 요청을 하면 에러가 발생한다.")
    public void requestCancel_NoOrderDetail_ERROR() {

        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("주문 상세 정보를 찾을 수 없습니다.")
                .satisfies(exception -> { CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(ORDERDETAIL_NOT_FOUND);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(NOT_FOUND);
                });

    }

    @Test
    @DisplayName("주문상세가 취소할 수 없는 상태라면 에러가 발생한다.")
    public void requestCancel_Status_ERROR() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestCancel(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("취소가 불가능한 상품입니다.")
                .satisfies(exception -> { CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_CANCELLABLE_PRODUCT);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("반품 요청 (WOW)")
    public void requestReturn_WOW() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestReturnHttp.Response> result = testContainer.orderCancelReturnController.requestReturn(orderDetailId, numberOfReturns, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getReturnRefundInfo().getDeliveryFee()).isEqualTo(WOW.getDeliveryFee());
        assertThat(result.getData().getReturnRefundInfo().getReturnFee()).isEqualTo(WOW.getReturnFee());
        assertThat(result.getData().getReturnRefundInfo().getProductTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("반품 요청 (BASIC)")
    public void requestReturn_BASIC() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestReturnHttp.Response> result = testContainer.orderCancelReturnController.requestReturn(orderDetailId, numberOfReturns, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getReturnRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getData().getReturnRefundInfo().getReturnFee()).isEqualTo(MemberShipPrice.BASIC.getReturnFee());
        assertThat(result.getData().getReturnRefundInfo().getProductTotalPrice()).isEqualTo(2000L);

        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(2L);
        assertThat(result.getData().getProductInfo().getImage()).isEqualTo("빵빵이썸네일");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("저장된 맴버가 없을 때 취소 요청을 하면 에러가 발생한다.")
    public void requestReturn_NoMember_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("저장된 주문 상세가 없을 때 취소 요청을 하면 에러가 발생한다.")
    public void requestReturn_NoOrderDetail_ERROR() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("주문 상세 정보를 찾을 수 없습니다.")
                .satisfies(exception -> { CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(ORDERDETAIL_NOT_FOUND);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(NOT_FOUND);
                });
    }

    @Test
    @DisplayName("주문상세가 반품할 수 없는 상태라면 에러가 발생한다.")
    public void requestReturn_Status_ERROR() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RETURN_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("반품이 불가능한 상품입니다.")
                .satisfies(exception -> { CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_RETURNABLE_PRODUCT);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

//    @Test
//    @DisplayName("")
//    public void getCancelReturnList() {
//        // given
//        Long memberId = 1L;
//        Integer page = 0;
//        Integer size = 5;
//
//        // when
//        ApiResponse<Page<GetCancelReturnListHttp.Response>> result = orderCancelReturnController.getCancelReturnList(memberId, page, size);
//
//        // then
//        assertThat(result.getCode()).isEqualTo(OK.value());
//        assertThat(result.getStatus()).isEqualTo(OK);
//        assertThat(result.getMessage()).isEqualTo(OK.name());
//        assertThat(result.getData().getContent().size()).isEqualTo(1);
//        assertThat(result.getData().getContent().get(0).getOrderNo()).isEqualTo("1");
//
//        List<GetCancelReturnListHttp.OrderDetailInfo> dtios = result.getData().getContent().get(0).getOrderDetailInfos();
//        assertThat(dtios.get(0).getOrderNo()).isEqualTo("1");
//        assertThat(dtios.get(0).getProductId()).isEqualTo(1L);
//        assertThat(dtios.get(0).getProductNo()).isEqualTo("1");
//        assertThat(dtios.get(0).getName()).isEqualTo("빵빵이키링");
//        assertThat(dtios.get(0).getPrice()).isEqualTo(1000L);
//        assertThat(dtios.get(0).getQuantity()).isEqualTo(2L);
//        assertThat(dtios.get(0).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
//
//        assertThat(dtios.get(1).getOrderNo()).isEqualTo("1");
//        assertThat(dtios.get(1).getProductId()).isEqualTo(2L);
//        assertThat(dtios.get(1).getProductNo()).isEqualTo("2");
//        assertThat(dtios.get(1).getName()).isEqualTo("옥지얌키링");
//        assertThat(dtios.get(1).getPrice()).isEqualTo(2000L);
//        assertThat(dtios.get(1).getQuantity()).isEqualTo(3L);
//        assertThat(dtios.get(1).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
//    }
//
//    @Test
//    @DisplayName("")
//    public void getCancelReturnDetail() {
//        // given
//        List<Long> orderDetailIds = List.of(1L, 2L);
//        Long memberId = 1L;
//
//        // when
//        ApiResponse<GetCancelReturnDetailHttp.Response> result = orderCancelReturnController.getCancelReturnDetail(orderDetailIds, memberId);
//
//        // then
//        assertThat(result.getCode()).isEqualTo(OK.value());
//        assertThat(result.getStatus()).isEqualTo(OK);
//        assertThat(result.getMessage()).isEqualTo(OK.name());
////        assertThat(result.getData().getOrderDate()).isEqualTo()
////        assertThat(result.getData().getCancelDate()).isEqualTo()
//        assertThat(result.getData().getOrderNo()).isEqualTo("1");
//        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");
//
//        assertThat(result.getData().getProductInfos().size()).isEqualTo(2);
//        assertThat(result.getData().getProductInfos().get(0).getProductId()).isEqualTo(1L);
//        assertThat(result.getData().getProductInfos().get(0).getProductNo()).isEqualTo("1");
//        assertThat(result.getData().getProductInfos().get(0).getName()).isEqualTo("빵빵이키링");
//        assertThat(result.getData().getProductInfos().get(0).getPrice()).isEqualTo(1000L);
//        assertThat(result.getData().getProductInfos().get(0).getQuantity()).isEqualTo(2L);
//
//        assertThat(result.getData().getProductInfos().get(1).getProductId()).isEqualTo(2L);
//        assertThat(result.getData().getProductInfos().get(1).getProductNo()).isEqualTo("2");
//        assertThat(result.getData().getProductInfos().get(1).getName()).isEqualTo("옥지얌키링");
//        assertThat(result.getData().getProductInfos().get(1).getPrice()).isEqualTo(2000L);
//        assertThat(result.getData().getProductInfos().get(1).getQuantity()).isEqualTo(3L);
//    }
}
