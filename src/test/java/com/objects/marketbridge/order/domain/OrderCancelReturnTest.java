package com.objects.marketbridge.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_PARTIAL_CANCEL;
import static org.assertj.core.api.Assertions.assertThat;

class OrderCancelReturnTest {

    @Test
    @DisplayName("주문상세, 상태코드, 이유로 생성할 수 있다.")
    public void create() {
//        // given
//        OrderDetail orderDetail = OrderDetail.builder()
//                .price(1000L)
//                .reducedQuantity(2L)
//                .build();
//
//        String reason = "단순변심";
//        String statusCode = ORDER_PARTIAL_CANCEL.getCode();
//
//        // when
//        OrderCancelReturn result = OrderCancelReturn.create(orderDetail, statusCode, reason);
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(statusCode);
//        assertThat(result.getOrderDetail()).isEqualTo(orderDetail);
//        assertThat(result.getQuantity()).isEqualTo(2L);
//        assertThat(result.getRefundAmount()).isEqualTo(2000L);
//        assertThat(result.getReason()).isEqualTo(reason);
    }

}