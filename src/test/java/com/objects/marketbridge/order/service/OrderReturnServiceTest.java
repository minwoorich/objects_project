package com.objects.marketbridge.order.service;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.order.mock.BaseFakeOrderRepository;
import com.objects.marketbridge.order.mock.TestContainer;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.service.dto.ConfirmCancelDto;
import com.objects.marketbridge.order.service.dto.GetCancelDetailDto;
import com.objects.marketbridge.order.service.dto.RequestCancelDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.product.domain.Product;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.member.domain.MembershipType.BASIC;
import static com.objects.marketbridge.member.domain.MembershipType.WOW;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class OrderReturnServiceTest {

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
                .reducedQuantity(0L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        String membership = WOW.getText();

        // when
        RequestReturnDto.Response result = testContainer.orderReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership);
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
                .reducedQuantity(0L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        testContainer.productRepository.save(product);
        testContainer.orderDetailCommendRepository.save(orderDetail);

        Long orderDetailId = 1L;
        Long numberOfReturns = 2L;
        String membership = BASIC.getText();

        // when
        RequestReturnDto.Response result = testContainer.orderReturnService.findReturnInfo(orderDetailId, numberOfReturns, membership);
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
    public void findReturnInfo_ENTITY_NOT_FOUND_ERROR() {
        // given
        Long orderDetailId = 1L;
        Long numberOfCancellation = 2L;
        String membership = BASIC.getText();

        // when
        assertThatThrownBy(() -> testContainer.orderReturnService.findReturnInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("엔티티가 존재하지 않습니다");
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
        assertThatThrownBy(() -> testContainer.orderReturnService.findReturnInfo(orderDetailId, numberOfCancellation, membership))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("반품이 불가능한 상품입니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_RETURNABLE_PRODUCT);
                });
    }

}