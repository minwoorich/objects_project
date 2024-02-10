package com.objects.marketbridge.order.controller;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.order.controller.dto.RequestCancelHttp;
import com.objects.marketbridge.order.controller.dto.RequestReturnHttp;
import com.objects.marketbridge.order.domain.MemberShipPrice;
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

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_CANCELLABLE_PRODUCT;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.NON_RETURNABLE_PRODUCT;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

class OrderReturnControllerTest {

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
                .reducedQuantity(0L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestReturnHttp.Response> result = testContainer.orderReturnController.requestReturn(orderDetailId, numberOfReturns, memberId);

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
                .reducedQuantity(0L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.memberRepository.save(member);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        Long memberId = 1L;

        // when
        ApiResponse<RequestReturnHttp.Response> result = testContainer.orderReturnController.requestReturn(orderDetailId, numberOfReturns, memberId);

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
        assertThatThrownBy(() -> testContainer.orderReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
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
        assertThatThrownBy(() -> testContainer.orderReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
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
        assertThatThrownBy(() -> testContainer.orderReturnController.requestReturn(orderDetailId, numberOfCancellation, memberId))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("반품이 불가능한 상품입니다.")
                .satisfies(exception -> { CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_RETURNABLE_PRODUCT);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }
}