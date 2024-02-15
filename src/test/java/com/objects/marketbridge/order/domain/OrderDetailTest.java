package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.order.mock.TestDateTimeHolder;
import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class OrderDetailTest {

    @Test
    @DisplayName("memberCoupon이 존재하면 memberCoupon의 상태를 바꾼다.")
    public void changeMemberCouponInfo_isNotNull() {
        // given
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(false)
                .usedDate(LocalDateTime.now())
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .build();

        // when
        orderDetail.changeMemberCouponInfo(null);

        // then
        assertThat(orderDetail.getMemberCoupon().getIsUsed()).isTrue();
        assertThat(orderDetail.getMemberCoupon().getUsedDate()).isNull();
    }

    @Test
    @DisplayName("memberCoupon이 없다면 memberCoupon의 상태를 바꾸지 않는다.")
    public void changeMemberCouponInfo_isNull() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(null)
                .build();

        // when
        orderDetail.changeMemberCouponInfo(null);

        // then
        assertThat(orderDetail.getMemberCoupon()).isNull();
    }

    @Test
    @DisplayName("memberCoupon이 존재하면 memberCoupon의 상태를 바꾼다.")
    public void returnMemberCoupon_isNotNull() {
        // given
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(false)
                .usedDate(LocalDateTime.now())
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(memberCoupon)
                .build();

        // when
        orderDetail.returnMemberCoupon();

        // then
        assertThat(orderDetail.getMemberCoupon().getIsUsed()).isTrue();
        assertThat(orderDetail.getMemberCoupon().getUsedDate()).isNull();
    }

    @Test
    @DisplayName("memberCoupon이 없다면 memberCoupon의 상태를 바꾸지 않는다.")
    public void returnMemberCoupon_isNull() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .memberCoupon(null)
                .build();

        // when
        orderDetail.returnMemberCoupon();

        // then
        assertThat(orderDetail.getMemberCoupon()).isNull();
    }

    @Test
    @DisplayName("취소된 상품의 총 금액을 반환한다.")
    public void cancelAmount() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(2L)
                .price(1000L)
                .build();

        // when
        Integer cancelAmount = orderDetail.cancelAmount();

        // then
        assertThat(cancelAmount).isEqualTo(2000L);
    }

    @Test
    @DisplayName("배송 완료된 상품은 취소할 수 없다.")
    public void cancel_DELIVERY_COMPLETED_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        LocalDateTime now = LocalDateTime.of(2024, 2, 9, 11, 27);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        // when // then
        assertThatThrownBy(() -> orderDetail.cancel(null,  dateTimeHolder))
                .hasMessage(NON_CANCELLABLE_PRODUCT.getMessage())
                .isInstanceOf(CustomLogicException.class)
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_CANCELLABLE_PRODUCT);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("부분취소된 상품은 취소할 수 없다.")
    public void cancel_ORDER_PARTIAL_CANCEL_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .statusCode(ORDER_PARTIAL_CANCEL.getCode())
                .build();

        LocalDateTime now = LocalDateTime.of(2024, 2, 9, 11, 27);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        // when // then
        assertThatThrownBy(() -> orderDetail.cancel(null, dateTimeHolder))
                .hasMessage(NON_CANCELLABLE_PRODUCT.getMessage())
                .isInstanceOf(CustomLogicException.class)
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_CANCELLABLE_PRODUCT);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("전체 수량보다 취소 수량이 많을 경우 에러가 발생한다.")
    public void cancel_QUANTITY_EXCEEDED_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(5L)
                .reducedQuantity(4L)
                .statusCode(ORDER_RECEIVED.getCode())
                .build();

        Long numberOfCancellations = 2L;
        LocalDateTime now = LocalDateTime.of(2024, 2, 9, 11, 27);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        // when // then
        assertThatThrownBy(() -> orderDetail.cancel(numberOfCancellations, dateTimeHolder))
                .hasMessage(QUANTITY_EXCEEDED.getMessage())
                .isInstanceOf(CustomLogicException.class)
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(QUANTITY_EXCEEDED);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("부분 취소의 경우 상품재고, 취소수량, 쿠폰상태가 변경된다.")
    public void cancel_partial() {
        // given
        Product product = Product.builder()
                .stock(5L)
                .build();

        LocalDateTime usedDate = LocalDateTime.of(2024, 2, 9, 11, 30);
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(true)
                .usedDate(usedDate)
                .build();

        String statusCode = PAYMENT_COMPLETED.getCode();
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(5L)
                .product(product)
                .memberCoupon(memberCoupon)
                .reducedQuantity(0L)
                .statusCode(statusCode)
                .build();

        Long numberOfCancellations = 2L;

        // when
        orderDetail.cancel(numberOfCancellations, null);

        // then
        assertThat(product.getStock()).isEqualTo(7L);
        assertThat(memberCoupon.getUsedDate()).isNull();
        assertThat(memberCoupon.getIsUsed()).isFalse();
        assertThat(orderDetail.getQuantity()).isEqualTo(5L);
        assertThat(orderDetail.getReducedQuantity()).isEqualTo(numberOfCancellations);
        assertThat(orderDetail.getStatusCode()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("모든 취소의 경우 상품재고, 취소수량, 쿠폰상태, 이유, 주문상태가 변경된다.")
    public void cancel() {
        // given
        Product product = Product.builder()
                .stock(5L)
                .build();

        LocalDateTime usedDate = LocalDateTime.of(2024, 2, 9, 11, 30);
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(true)
                .usedDate(usedDate)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(5L)
                .product(product)
                .memberCoupon(memberCoupon)
                .reducedQuantity(0L)
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        Long numberOfCancellations = 5L;
        String reason = "단순변심";

        // when
        orderDetail.cancel(numberOfCancellations, null);

        // then
        assertThat(product.getStock()).isEqualTo(10L);
        assertThat(memberCoupon.getUsedDate()).isNull();
        assertThat(memberCoupon.getIsUsed()).isFalse();
        assertThat(orderDetail.getQuantity()).isEqualTo(5L);
        assertThat(orderDetail.getReducedQuantity()).isEqualTo(numberOfCancellations);
        assertThat(orderDetail.getStatusCode()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("배송완료된 상태를 제외하면 에러를 발생시킨다.")
    public void returns_NOT_DELIVERY_COMPLETED_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .statusCode(ORDER_RECEIVED.getCode())
                .build();
        LocalDateTime now = LocalDateTime.of(2024, 2, 10, 5, 3);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        // when // then
        assertThatThrownBy(() -> orderDetail.returns( null, dateTimeHolder))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage(NON_RETURNABLE_PRODUCT.getMessage())
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                    assertThat(customLogicException.getErrorCode()).isEqualTo(NON_RETURNABLE_PRODUCT);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                });
    }

    @Test
    @DisplayName("반품 수량을 초과하면 에러를 발생시킨다.")
    public void returns_QUANTITY_EXCEEDED_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .statusCode(DELIVERY_COMPLETED.getCode())
                .quantity(5L)
                .reducedQuantity(0L)
                .build();
        LocalDateTime now = LocalDateTime.of(2024, 2, 10, 5, 3);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        Long numberOfReturns = 6L;

        // when // then
        assertThatThrownBy(() -> orderDetail.returns( numberOfReturns, dateTimeHolder))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("수량이 초과 되었습니다.")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                    assertThat(customLogicException.getErrorCode()).isEqualTo(QUANTITY_EXCEEDED);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                });
    }

    @Test
    @DisplayName("부분 반품하면 상품재고, 수량감소, 쿠폰 반환만 한다.")
    public void returns_partial() {
        // given
        Product product = Product.builder()
                .stock(5L)
                .build();

        LocalDateTime usedDate = LocalDateTime.of(2024, 2, 9, 11, 30);
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(true)
                .usedDate(usedDate)
                .build();

        String statusCode = DELIVERY_COMPLETED.getCode();
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(5L)
                .product(product)
                .memberCoupon(memberCoupon)
                .reducedQuantity(0L)
                .statusCode(statusCode)
                .build();

        LocalDateTime now = LocalDateTime.of(2024, 2, 10, 5, 15);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        Long numberOfReturns = 2L;

        // when
        orderDetail.returns(numberOfReturns, dateTimeHolder);

        // then
        assertThat(product.getStock()).isEqualTo(7L);
        assertThat(memberCoupon.getUsedDate()).isNull();
        assertThat(memberCoupon.getIsUsed()).isFalse();
        assertThat(orderDetail.getQuantity()).isEqualTo(5L);
        assertThat(orderDetail.getReducedQuantity()).isEqualTo(numberOfReturns);
        assertThat(orderDetail.getStatusCode()).isEqualTo(RETURN_INIT.getCode());
    }

    @Test
    @DisplayName("모두 반품하면 상품재고, 수량감소, 쿠폰 반환, 상태변화, 이유를 변경한다.")
    public void returns() {
        // given
        Product product = Product.builder()
                .stock(5L)
                .build();

        LocalDateTime usedDate = LocalDateTime.of(2024, 2, 9, 11, 30);
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .isUsed(true)
                .usedDate(usedDate)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(5L)
                .product(product)
                .memberCoupon(memberCoupon)
                .reducedQuantity(0L)
                .statusCode(DELIVERY_COMPLETED.getCode())
                .build();

        LocalDateTime now = LocalDateTime.of(2024, 2, 10, 5, 16);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(now)
                .build();

        Long numberOfReturns = 5L;

        // when
        orderDetail.returns(numberOfReturns, dateTimeHolder);

        // then
        assertThat(product.getStock()).isEqualTo(10L);
        assertThat(memberCoupon.getUsedDate()).isNull();
        assertThat(memberCoupon.getIsUsed()).isFalse();
        assertThat(orderDetail.getQuantity()).isEqualTo(5L);
        assertThat(orderDetail.getReducedQuantity()).isEqualTo(numberOfReturns);
        assertThat(orderDetail.getStatusCode()).isEqualTo(RETURN_INIT.getCode());
    }

    @Test
    @DisplayName("주어진 수량만큼 합계를 반환한다.")
    public void totalAmount() {
        // given
        Long numberOfCancellations = 3L;
        OrderDetail orderDetail = createOrderDetail(10L, 1000L);

        // when
        Integer result = orderDetail.totalAmount(numberOfCancellations, LocalDateTime.now());

        // then
        assertThat(result).isEqualTo(3000L);
    }

    @Test
    @DisplayName("취소한 수량이 기존 수량보다 많다면 에러를 생성한다.")
    public void totalAmount_with_error() {
        // given
        Long numberOfCancellations = 11L;
        OrderDetail orderDetail = createOrderDetail(10L, 1000L);

        LocalDateTime now = LocalDateTime.of(2024, 2, 9, 11, 27);
        // when // then
        assertThatThrownBy(() -> orderDetail.totalAmount(numberOfCancellations, now))
                .hasMessage("수량이 초과 되었습니다.")
                .isInstanceOf(CustomLogicException.class)
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(QUANTITY_EXCEEDED);
                    assertThat(customLogicException.getTimestamp()).isEqualTo(now);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("수량이 주어지지 않으면 모든 수량에 대한 합계를 반환한다.")
    public void totalAmount_All() {
        // given
        OrderDetail orderDetail = createOrderDetail(10L, 1000L);

        // when
        Integer result = orderDetail.totalAmount();

        // then
        assertThat(result).isEqualTo(10000L);
    }

    @Test
    @DisplayName("반품 철회시 여러 상태값이 바뀐다.")
    public void withdraw() {
        // given
        Product product = Product.builder()
                .stock(10L)
                .build();

        Coupon coupon = Coupon.builder()
                .product(product)
                .price(1000L)
                .build();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .usedDate(LocalDateTime.now())
                .isUsed(false)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 2, 1, 14, 12, 40);
        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .memberCoupon(memberCoupon)
                .reducedQuantity(3L)
                .cancelledAt(cancelledAt)
                .statusCode(ORDER_PARTIAL_RETURN.getCode())
                .build();

        Long withdrawQuantity = 3L;
        String previousStatusCode = DELIVERY_COMPLETED.getCode();

        // when
        orderDetail.withdraw(withdrawQuantity, previousStatusCode);

        // then
        assertThat(product.getStock()).isEqualTo(7L);
        assertThat(memberCoupon.getUsedDate()).isEqualTo(cancelledAt);
        assertThat(memberCoupon.getIsUsed()).isTrue();
        assertThat(orderDetail.getReducedQuantity()).isEqualTo(0L);
        assertThat(orderDetail.getStatusCode()).isEqualTo(previousStatusCode);
    }

    @Test
    @DisplayName("철회했던 수량보다 많은 양을 철회하면 예외가 발생한다.")
    public void withdraw_OUT_OF_WITHDRAW_QUANTITY_ERROR() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .reducedQuantity(3L)
                .build();

        Long withdrawQuantity = 4L;

        // when // then
        assertThatThrownBy(() -> orderDetail.withdraw(withdrawQuantity, null))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage(OUT_OF_WITHDRAW_QUANTITY.getMessage())
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(OUT_OF_WITHDRAW_QUANTITY);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    @Test
    @DisplayName("철회한 수량이 남아있는 상품 재고보다 많을경우 에러가 발생한다.")
    public void withdraw_OUT_OF_STOCK_ERROR() {
        // given
        Product product = Product.builder()
                .stock(1L)
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .reducedQuantity(3L)
                .build();

        Long withdrawQuantity = 2L;

        // when // then
        assertThatThrownBy(() -> orderDetail.withdraw(withdrawQuantity, null))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("재고가 부족합니다")
                .satisfies(exception -> {
                    CustomLogicException customLogicException = (CustomLogicException) exception;
                    assertThat(customLogicException.getErrorCode()).isEqualTo(OUT_OF_STOCK);
                    assertThat(customLogicException.getHttpStatus()).isEqualTo(BAD_REQUEST);
                });
    }

    private static OrderDetail createOrderDetail(long quantity, long price) {
        return OrderDetail.builder()
                .quantity(quantity)
                .price(price)
                .reducedQuantity(0L)
                .build();
    }

    private OrderDetail createOrderDetail(String statusCode, Product product, Long quantity, long price) {
        return OrderDetail
                .builder()
                .product(product)
                .statusCode(statusCode)
                .quantity(quantity)
                .price(price)
                .build();
    }

    private OrderDetail createOrderDetail(String statusCode, String reason, long quantity) {
        return OrderDetail
                .builder()
                .product(Product.builder()
                        .stock(10L)
                        .build())
                .statusCode(statusCode)
                .quantity(quantity)
                .build();
    }


}