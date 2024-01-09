package com.objects.marketbridge.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdOrderTest {

    @Test
    @DisplayName("주문 상세 리스트를 가져온다.")
    public void getOrderDetails() {
        // given
        ProdOrderDetail orderDetail1 = createOrderDetail();
        ProdOrderDetail orderDetail2 = createOrderDetail();
        ProdOrderDetail orderDetail3 = createOrderDetail();
        List<ProdOrderDetail> orderDetails = List.of(orderDetail1, orderDetail2, orderDetail3);

        ProdOrder order = createOrder(orderDetails);

        // when
        List<ProdOrderDetail> getOrderDetails = order.getOrderDetails();

        // then
        Assertions.assertThat(getOrderDetails.size()).isEqualTo(3);
    }

    private ProdOrder createOrder(List<ProdOrderDetail> orderDetails) {
        return ProdOrder.builder()
                .orderDetails(orderDetails)
                .build();
    }

    private ProdOrderDetail createOrderDetail() {
        return ProdOrderDetail.builder().build();
    }

}