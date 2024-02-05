package com.objects.marketbridge.order.infra.dtio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancelReturnResponseDtioTest {

    @Test
    @DisplayName("주문 상세 반환 리스트를 바꿀 수 있다.")
    public void changeDetailResponseDaos() {
        // given
        GetCancelReturnListDtio.Response cancelReturnResponseDtio = GetCancelReturnListDtio.Response.builder()
                .build();

        GetCancelReturnListDtio.OrderDetailInfo detailResponseDtio1 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .productId(1L)
                .build();
        GetCancelReturnListDtio.OrderDetailInfo detailResponseDtio2 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .productId(2L)
                .build();
        List<GetCancelReturnListDtio.OrderDetailInfo> list = List.of(detailResponseDtio1, detailResponseDtio2);

        // when
        cancelReturnResponseDtio.changeOrderDetailInfos(list);

        // then
        assertThat(cancelReturnResponseDtio.getOrderDetailInfos()).hasSize(2)
                .extracting("productId")
                .contains(1L, 2L);
    }

    @Test
    @DisplayName("주문 상세 반환 리스트가 주어지지 않으면 에러를 발생시킨다.")
    public void changeDetailResponseDaosWithError() {
        // given
        GetCancelReturnListDtio.Response cancelReturnResponseDtio = GetCancelReturnListDtio.Response.builder()
                .build();

        // when // then
        assertThatThrownBy(() -> cancelReturnResponseDtio.changeOrderDetailInfos(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주어진 주문 상세 리스트가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주어진 주문 상세 반환 리스트가 빈 값 일 수 있다.")
    public void changeDetailResponseDaosWithEmptyList() {
        // given
        GetCancelReturnListDtio.Response cancelReturnResponseDtio = GetCancelReturnListDtio.Response.builder()
                .build();
        List<GetCancelReturnListDtio.OrderDetailInfo> list = List.of();

        // when
        cancelReturnResponseDtio.changeOrderDetailInfos(list);

        // then
        assertThat(cancelReturnResponseDtio.getOrderDetailInfos()).hasSize(0);
    }

}