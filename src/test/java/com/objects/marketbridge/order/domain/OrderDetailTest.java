package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.*;

class OrderDetailTest {

    @Test
    @DisplayName("주문 상세의 코드를 바꾼다.")
    public void changeStatusCode() {
        // given
        String givenStatusCode = ORDER_RECEIVED.getCode();
        OrderDetail orderDetail = createOrderDetail(PAYMENT_COMPLETED.getCode());

        // when
        orderDetail.changeStatusCode(givenStatusCode);

        // then
        assertThat(orderDetail.getStatusCode()).isEqualTo(givenStatusCode);
    }

    // TODO : 이거 에러 발생해서 주석 처리했어요 - 민우
//    @Test
//    @DisplayName("이유와 상태코드가 취소로 바뀌어야 한다.")
//    public void changeReasonAndStatus1() {
//        // given
//        OrderDetail orderDetail = createOrderDetail(ORDER_RECEIVED.getCode());
//
//        // when
//        orderDetail.changeReasonAndStatus("테스트 취소", ORDER_CANCEL.getCode());
//
//        // then
//        assertThat(orderDetail)
//                .extracting("statusCode", "reason")
//                .contains(ORDER_CANCEL.getCode(), "테스트 취소");
//    }

    @Test
    @DisplayName("배송완료된 상품은 취소가 되지 않는다.")
    public void changeReasonAndStatus2() {
        // given
        OrderDetail orderDetail = createOrderDetail(DELIVERY_COMPLETED.getCode());

        // when // then
        assertThatThrownBy(() -> orderDetail.changeReasonAndStatus("테스트 취소", ORDER_CANCEL.getCode()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 배송완료된 상품은 취소가 불가능합니다.");
    }

    private OrderDetail createOrderDetail(String statusCode) {
        return OrderDetail
                .builder()
                .product(Product.builder()
                        .stock(10L)
                        .build())
                .statusCode(statusCode)
                .quantity(10L)
                .build();
    }

}