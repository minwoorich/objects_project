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
        CancelReturnResponseDtio cancelReturnResponseDtio = CancelReturnResponseDtio.builder()
                .build();

        DetailResponseDtio detailResponseDtio1 = DetailResponseDtio.builder()
                .productId(1L)
                .build();
        DetailResponseDtio detailResponseDtio2 = DetailResponseDtio.builder()
                .productId(2L)
                .build();
        List<DetailResponseDtio> list = List.of(detailResponseDtio1, detailResponseDtio2);

        // when
        cancelReturnResponseDtio.changeDetailResponsDaos(list);

        // then
        assertThat(cancelReturnResponseDtio.getDetailResponseDtios()).hasSize(2)
                .extracting("productId")
                .contains(1L, 2L);
    }

    @Test
    @DisplayName("주문 상세 반환 리스트가 주어지지 않으면 에러를 발생시킨다.")
    public void changeDetailResponseDaosWithError() {
        // given
        CancelReturnResponseDtio cancelReturnResponseDtio = CancelReturnResponseDtio.builder()
                .build();

        // when // then
        assertThatThrownBy(() -> cancelReturnResponseDtio.changeDetailResponsDaos(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주어진 주문 상세 리스트가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주어진 주문 상세 반환 리스트가 빈 값 일 수 있다.")
    public void changeDetailResponseDaosWithEmptyList() {
        // given
        CancelReturnResponseDtio cancelReturnResponseDtio = CancelReturnResponseDtio.builder()
                .build();
        List<DetailResponseDtio> list = List.of();

        // when
        cancelReturnResponseDtio.changeDetailResponsDaos(list);

        // then
        assertThat(cancelReturnResponseDtio.getDetailResponseDtios()).hasSize(0);
    }

}