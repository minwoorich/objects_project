package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.order.controller.dto.*;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.order.mock.BaseFakeOrderRepository;
import com.objects.marketbridge.order.mock.TestContainer;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.*;

public class OrderCancelReturnControllerTest {

    private LocalDateTime orderDate = LocalDateTime.of(2024, 2, 9, 3, 9);
    private TestContainer testContainer = TestContainer.builder()
            .dateTimeHolder(
                    TestDateTimeHolder.builder()
                            .createTime(orderDate)
                            .build()
            )
            .build();

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
    public void requestCancel_ENTITY_NOT_FOUND_ERROR() {

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
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("엔티티가 존재하지 않습니다");

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
    public void requestReturn_ENTITY_NOT_FOUND_ERROR() {
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
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("엔티티가 존재하지 않습니다");
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

    @Test
    @DisplayName("취소/반품한 상품들을 조회할 수 있다.")
    public void getCancelReturnList() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product1 = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .productNo("1")
                .build();
        Product product2 = Product.builder()
                .name("옥지얌키링")
                .thumbImg("옥지얌썸네일")
                .productNo("2")
                .build();

        LocalDateTime orderDate1 = LocalDateTime.of(2024, 2, 8, 9, 30);
        LocalDateTime orderDate2 = LocalDateTime.of(2024, 2, 8, 9, 31);
        LocalDateTime cancelDate1 = LocalDateTime.of(2024, 2, 8, 9, 32);
        LocalDateTime cancelDate2 = LocalDateTime.of(2024, 2, 8, 9, 33);

        Order order = Order.builder()
                .member(member)
                .orderNo("1")
                .build();

        OrderDetail orderDetail1 = OrderDetail.builder()
                .order(order)
                .orderNo("1")
                .quantity(10L)
                .product(product1)
                .price(1000L)
                .statusCode(ORDER_CANCEL.getCode())
                .cancelledAt(cancelDate1)
                .build();
        ReflectionTestUtils.setField(orderDetail1, "createdAt", orderDate1, LocalDateTime.class);
        OrderDetail orderDetail2 = OrderDetail.builder()
                .order(order)
                .orderNo("1")
                .quantity(10L)
                .product(product2)
                .price(2000L)
                .statusCode(ORDER_CANCEL.getCode())
                .cancelledAt(cancelDate2)
                .build();
        ReflectionTestUtils.setField(orderDetail2, "createdAt", orderDate2, LocalDateTime.class);

        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);

        testContainer.orderCommendRepository.save(order);
        testContainer.productRepository.save(product1);
        testContainer.productRepository.save(product2);
        testContainer.orderDetailCommendRepository.save(orderDetail1);
        testContainer.orderDetailCommendRepository.save(orderDetail2);
        testContainer.memberRepository.save(member);

        Integer page = 0;
        Integer size = 5;
        Long memberId = 1L;

        // when
        ApiResponse<Page<GetCancelReturnListHttp.Response>> result = testContainer.orderCancelReturnController.getCancelReturnList(page, size, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());
        assertThat(result.getData().getContent().size()).isEqualTo(2);
        assertThat(result.getData().getContent().get(0).getCancelReceiptDate()).isEqualTo(cancelDate1);
        assertThat(result.getData().getContent().get(0).getOrderDate()).isEqualTo(orderDate1);
        assertThat(result.getData().getContent().get(1).getCancelReceiptDate()).isEqualTo(cancelDate2);
        assertThat(result.getData().getContent().get(1).getOrderDate()).isEqualTo(orderDate2);

        GetCancelReturnListHttp.OrderDetailInfo orderDetailInfo1 = result.getData().getContent().get(0).getOrderDetailInfo();
        assertThat(orderDetailInfo1.getOrderNo()).isEqualTo("1");
        assertThat(orderDetailInfo1.getProductId()).isEqualTo(1L);
        assertThat(orderDetailInfo1.getProductNo()).isEqualTo("1");
        assertThat(orderDetailInfo1.getName()).isEqualTo("빵빵이키링");
        assertThat(orderDetailInfo1.getPrice()).isEqualTo(1000L);
        assertThat(orderDetailInfo1.getQuantity()).isEqualTo(10L);
        assertThat(orderDetailInfo1.getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());

        GetCancelReturnListHttp.OrderDetailInfo orderDetailInfo2 = result.getData().getContent().get(1).getOrderDetailInfo();
        assertThat(orderDetailInfo2.getOrderNo()).isEqualTo("1");
        assertThat(orderDetailInfo2.getProductId()).isEqualTo(2L);
        assertThat(orderDetailInfo2.getProductNo()).isEqualTo("2");
        assertThat(orderDetailInfo2.getName()).isEqualTo("옥지얌키링");
        assertThat(orderDetailInfo2.getPrice()).isEqualTo(2000L);
        assertThat(orderDetailInfo2.getQuantity()).isEqualTo(10L);
        assertThat(orderDetailInfo2.getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("취소/반품 상세 조회 (WOW_NoCoupon)")
    public void getCancelDetail_WOW_NoCoupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 2, 9, 3, 10);
        OrderDetail orderDetail = OrderDetail.builder()
                .orderNo("1")
                .reason("단순변심")
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RELEASE_PENDING.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when
        ApiResponse<GetCancelDetailHttp.Response> result = testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getData().getCancelDate()).isEqualTo(cancelledAt);
        assertThat(result.getData().getOrderNo()).isEqualTo("1");
        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getData().getProductInfo().getProductId()).isEqualTo(1L);
        assertThat(result.getData().getProductInfo().getProductNo()).isEqualTo("1");
        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(10L);

        assertThat(result.getData().getRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getData().getRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(result.getData().getRefundInfo().getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getData().getRefundInfo().getTotalPrice()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("취소/반품 상세 조회 (BASIC_NoCoupon)")
    public void getCancelDetail_BASIC_NoCoupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 2, 9, 3, 10);
        OrderDetail orderDetail = OrderDetail.builder()
                .orderNo("1")
                .reason("단순변심")
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RELEASE_PENDING.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when
        ApiResponse<GetCancelDetailHttp.Response> result = testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getData().getCancelDate()).isEqualTo(cancelledAt);
        assertThat(result.getData().getOrderNo()).isEqualTo("1");
        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getData().getProductInfo().getProductId()).isEqualTo(1L);
        assertThat(result.getData().getProductInfo().getProductNo()).isEqualTo("1");
        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(10L);

        assertThat(result.getData().getRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getData().getRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getData().getRefundInfo().getDiscountPrice()).isEqualTo(0L);
        assertThat(result.getData().getRefundInfo().getTotalPrice()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("취소/반품 상세 조회 (WOW_Coupon)")
    public void getCancelDetail_WOW_Coupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 2, 9, 3, 10);
        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .orderNo("1")
                .reason("단순변심")
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RELEASE_PENDING.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when
        ApiResponse<GetCancelDetailHttp.Response> result = testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getData().getCancelDate()).isEqualTo(cancelledAt);
        assertThat(result.getData().getOrderNo()).isEqualTo("1");
        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getData().getProductInfo().getProductId()).isEqualTo(1L);
        assertThat(result.getData().getProductInfo().getProductNo()).isEqualTo("1");
        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(10L);

        assertThat(result.getData().getRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getData().getRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(result.getData().getRefundInfo().getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getData().getRefundInfo().getTotalPrice()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("취소/반품 상세 조회 (BASIC_Coupon)")
    public void getCancelDetail_BASIC_Coupon() {
        // given
        Member member = Member.builder()
                .membership(MembershipType.BASIC.getText())
                .build();

        Product product = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 2, 9, 3, 10);
        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .orderNo("1")
                .reason("단순변심")
                .cancelledAt(cancelledAt)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RELEASE_PENDING.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);
        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when
        ApiResponse<GetCancelDetailHttp.Response> result = testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId);

        // then
        assertThat(result.getCode()).isEqualTo(OK.value());
        assertThat(result.getStatus()).isEqualTo(OK);
        assertThat(result.getMessage()).isEqualTo(OK.name());

        assertThat(result.getData().getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getData().getCancelDate()).isEqualTo(cancelledAt);
        assertThat(result.getData().getOrderNo()).isEqualTo("1");
        assertThat(result.getData().getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getData().getProductInfo().getProductId()).isEqualTo(1L);
        assertThat(result.getData().getProductInfo().getProductNo()).isEqualTo("1");
        assertThat(result.getData().getProductInfo().getName()).isEqualTo("빵빵이키링");
        assertThat(result.getData().getProductInfo().getPrice()).isEqualTo(1000L);
        assertThat(result.getData().getProductInfo().getQuantity()).isEqualTo(10L);

        assertThat(result.getData().getRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getData().getRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getData().getRefundInfo().getDiscountPrice()).isEqualTo(1000L);
        assertThat(result.getData().getRefundInfo().getTotalPrice()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("저장된 맴버가 없을 때 취소 요청을 하면 에러가 발생한다.")
    public void getCancelDetail_NoMember_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when // then
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("주문 상세가 없을 경우 에러가 발생한다.")
    public void getCancelDetail_ENTITY_NOT_FOUND_ERROR() {
        // given
        Member member = Member.builder().build();

        testContainer.memberRepository.save(member);

        Long orderDetailId = 1L;
        Long memberId = 1L;

        // when
        assertThatThrownBy(() -> testContainer.orderCancelReturnController.getCancelDetail(orderDetailId, memberId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("엔티티가 존재하지 않습니다");
    }

}
