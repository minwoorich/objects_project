package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("주문 상세의 코드를 바꾼다.")
    public void changeStatusCode() {
        // given
        String givenStatusCode = ORDER_RECEIVED.getCode();
        OrderDetail orderDetail = OrderDetail.builder()
                .statusCode(PAYMENT_COMPLETED.getCode())
                .build();

        // when
        orderDetail.changeStatusCode(givenStatusCode);

        // then
        assertThat(orderDetail.getStatusCode()).isEqualTo(givenStatusCode);
    }

    @Test
    @DisplayName("상태코드(취소), 이유, 수량감소, 상품 재고가 증가한다.")
    public void changeReasonAndStatus1() {
        // given
        Product product = Product.builder()
                .stock(10L)
                .build();

        OrderDetail orderDetail = createOrderDetail(ORDER_RECEIVED.getCode(), product,5L, 1000L);

        // when
        Integer result = orderDetail.changeReasonAndStatus("단순 변심", ORDER_CANCEL.getCode(), 2L);

        // then
        assertThat(orderDetail)
                .extracting("statusCode", "reason", "quantity")
                .contains(ORDER_CANCEL.getCode(), "단순 변심", 3L);

        assertThat(orderDetail.getProduct().getStock()).isEqualTo(12L);
        assertThat(result).isEqualTo(2000L);
    }

    @Test
    @DisplayName("배송완료된 상품은 취소가 되지 않는다.")
    public void changeReasonAndStatus2() {
        // given
        Product product = Product.builder()
                .stock(10L)
                .build();

        OrderDetail orderDetail = createOrderDetail(DELIVERY_COMPLETED.getCode(), product,5L, 1000L);
        Long numberOfCancellations = 3L;

        // when // then
        assertThatThrownBy(() -> orderDetail.changeReasonAndStatus("단순 변심", ORDER_CANCEL.getCode(), numberOfCancellations))
                .isInstanceOf(CustomLogicException.class)
                .hasMessage("취소가 불가능한 상품입니다.");
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

        // when // then
        assertThatThrownBy(() -> orderDetail.totalAmount(numberOfCancellations, LocalDateTime.now()))
                .hasMessage("수량이 초과 되었습니다.")
                .isInstanceOf(CustomLogicException.class);
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

    private static OrderDetail createOrderDetail(long quantity, long price) {
        return OrderDetail.builder()
                .quantity(quantity)
                .price(price)
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
                .reason(reason)
                .build();
    }

}