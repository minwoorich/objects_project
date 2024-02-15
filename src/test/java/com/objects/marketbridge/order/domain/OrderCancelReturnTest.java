package com.objects.marketbridge.order.domain;

import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.member.domain.MemberCoupon;
import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.OUT_OF_STOCK;
import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.OUT_OF_WITHDRAW_QUANTITY;
import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class OrderCancelReturnTest {

    @Test
    @DisplayName("주문상세, 상태코드, 이유로 생성할 수 있다.")
    public void create() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
                .price(1000L)
                .reducedQuantity(2L)
                .build();

        String reason = "단순변심";

        CancelReturnStatusCode statusCode = CancelReturnStatusCode.builder()
                .previousStatusCode(DELIVERY_COMPLETED.getCode())
                .statusCode(ORDER_PARTIAL_RETURN.getCode())
                .build();

        // when
        OrderCancelReturn result = OrderCancelReturn.create(orderDetail, statusCode, reason);

        // then
        assertThat(result.getStatusCode()).isEqualTo(statusCode.getStatusCode());
        assertThat(result.getOrderDetail()).isEqualTo(orderDetail);
        assertThat(result.getQuantity()).isEqualTo(2L);
        assertThat(result.getRefundAmount()).isEqualTo(2000L);
        assertThat(result.getReason()).isEqualTo(reason);
    }

}