package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.order.mock.BaseFakeOrderRepository;
import com.objects.marketbridge.order.mock.TestContainer;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.service.dto.RequestCancelDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.member.domain.MembershipType.BASIC;
import static com.objects.marketbridge.member.domain.MembershipType.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderCancelReturnServiceTest {

    private TestContainer testContainer = TestContainer.builder().build();

    @AfterEach
    void afterEach() {
        BaseFakeOrderDetailRepository.getInstance().clear();
        BaseFakeOrderRepository.getInstance().clear();
    }

//    @Test
//    @DisplayName("취소/반품 확정")
//    public void confirmCancelReturn() {
//        // given
//        ConfirmCancelReturnDto.OrderDetailInfo orderDetailInfo1 = ConfirmCancelReturnDto.OrderDetailInfo.builder()
//                .orderDetailId(1L)
//                .numberOfCancellation(1L)
//                .build();
//        ConfirmCancelReturnDto.OrderDetailInfo orderDetailInfo2 = ConfirmCancelReturnDto.OrderDetailInfo.builder()
//                .orderDetailId(2L)
//                .numberOfCancellation(2L)
//                .build();
//        List<ConfirmCancelReturnDto.OrderDetailInfo> orderDetailInfos = List.of(orderDetailInfo1, orderDetailInfo2);
//
//        ConfirmCancelReturnDto.Request request = ConfirmCancelReturnDto.Request.builder()
//                .orderDetailInfos(orderDetailInfos)
//                .cancelReason("단순변심")
//                .build();
//
//        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 31, 6, 7);
//        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
//                .updateTime(updateTime)
//                .build();
//
//        // when
//        ConfirmCancelReturnDto.Response result = orderCancelReturnService.confirmCancelReturn(request, dateTimeHolder);
//
//        // then
//        assertThat(result.getOrderId()).isEqualTo(1L);
//        assertThat(result.getOrderNo()).isEqualTo("1");
//        assertThat(result.getTotalPrice()).isEqualTo(30000L);
//        assertThat(result.getCancellationDate()).isEqualTo(updateTime);
//        assertThat(result.getRefundInfo())
//                .extracting("totalRefundAmount", "refundMethod", "refundProcessedAt")
//                .contains(5000L, "카드", cancelDate);
//        assertThat(result.getCancelledItems()).hasSize(2)
//                .extracting("productId", "productNo", "name", "price", "quantity")
//                .contains(
//                        tuple(1L, "1", "빵빵이키링", 1000L, 1L),
//                        tuple(2L, "2", "옥지얌키링", 2000L, 2L)
//                );
//    }

    @Test
    @DisplayName("주문 취소 요청 (WOW_NoCoupon)")
    public void findCancelInfo_WOW_NoCoupon() {
        // given
        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(ORDER_RECEIVED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = WOW.getText();

        // when
        RequestCancelDto.Response result = testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        RequestCancelDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(0L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 취소 요청 (BASIC_NoCoupon)")
    public void findCancelInfo_BASIC_NoCoupon() {
        // given
        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(ORDER_RECEIVED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        RequestCancelDto.Response result = testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        RequestCancelDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(0L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 취소 요청 (WOW_Coupon)")
    public void findCancelInfo_WOW_Coupon() {
        // given
        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(ORDER_RECEIVED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = WOW.getText();

        // when
        RequestCancelDto.Response result = testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        RequestCancelDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(1000L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 취소 요청 (BASIC_Coupon)")
    public void findCancelInfo_BASIC_Coupon() {
        // given
        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(ORDER_RECEIVED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        RequestCancelDto.Response result = testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        RequestCancelDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(1000L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 상세가 없을 경우 에러가 발생한다.")
    public void findCancelInfo_ORDERDETAIL_NOT_FOUND_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        assertThatThrownBy(() -> testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("주문 상세 정보를 찾을 수 없습니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(ORDERDETAIL_NOT_FOUND);
                });
    }

    @Test
    @DisplayName("배송 완료된 상품은 취소가 불가능하다.")
    public void findCancelInfo_NON_CANCELLABLE_PRODUCT_ERROR() {
        // given
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

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        assertThatThrownBy(() -> testContainer.orderCancelReturnService.findCancelInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("취소가 불가능한 상품입니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_CANCELLABLE_PRODUCT);
                });
    }

    @Test
    @DisplayName("주문 취소 요청 (WOW)")
    public void findReturnInfo_WOW() {
        // given
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

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        String membership = WOW.getText();

        // when
        RequestReturnDto.Response result = testContainer.orderCancelReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership);
        RequestReturnDto.ReturnRefundInfo returnRefundInfo = result.getReturnRefundInfo();
        RequestReturnDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(returnRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(returnRefundInfo.getReturnFee()).isEqualTo(MemberShipPrice.WOW.getReturnFee());
        assertThat(returnRefundInfo.getProductTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 반품 요청 (BASIC)")
    public void findReturnInfo_BASIC() {
        // given
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

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        String membership = BASIC.getText();

        // when
        RequestReturnDto.Response result = testContainer.orderCancelReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership);
        RequestReturnDto.ReturnRefundInfo returnRefundInfo = result.getReturnRefundInfo();
        RequestReturnDto.ProductInfo productInfo = result.getProductInfo();

        // then
        assertThat(returnRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(returnRefundInfo.getReturnFee()).isEqualTo(MemberShipPrice.BASIC.getReturnFee());
        assertThat(returnRefundInfo.getProductTotalPrice()).isEqualTo(2000L);

        assertThat(productInfo.getQuantity()).isEqualTo(2L);
        assertThat(productInfo.getName()).isEqualTo("빵빵이키링");
        assertThat(productInfo.getPrice()).isEqualTo(1000L);
        assertThat(productInfo.getImage()).isEqualTo("빵빵이썸네일");
    }

    @Test
    @DisplayName("주문 상세가 없을 경우 에러가 발생한다.")
    public void findReturnInfo_ORDERDETAIL_NOT_FOUND_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        assertThatThrownBy(() -> testContainer.orderCancelReturnService.findReturnInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("주문 상세 정보를 찾을 수 없습니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(ORDERDETAIL_NOT_FOUND);
                });
    }

    @Test
    @DisplayName("배송 완료된 상품은 취소가 불가능하다.")
    public void findReturnInfo_NON_RETURNABLE_PRODUCT_ERROR() {
        // given
        Product product = Product.builder()
                .name("빵빵이키링")
                .thumbImg("빵빵이썸네일")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(10L)
                .product(product)
                .price(1000L)
                .statusCode(RELEASE_PENDING.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        assertThatThrownBy(() -> testContainer.orderCancelReturnService.findReturnInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("반품이 불가능한 상품입니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_RETURNABLE_PRODUCT);
                });
    }

}