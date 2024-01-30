package com.objects.marketbridge.order.infra.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancelReturnResponseDaoTest {

    @Test
    @DisplayName("주문 상세 반환 리스트를 바꿀 수 있다.")
    public void changeDetailResponseDaos() {
        // given
        CancelReturnResponseDao cancelReturnResponseDao = CancelReturnResponseDao.builder()
                .build();

        DetailResponseDao detailResponseDao1 = DetailResponseDao.builder()
                .productId(1L)
                .build();
        DetailResponseDao detailResponseDao2 = DetailResponseDao.builder()
                .productId(2L)
                .build();
        List<DetailResponseDao> list = List.of(detailResponseDao1, detailResponseDao2);

        // when
        cancelReturnResponseDao.changeDetailResponsDaos(list);

        // then
        assertThat(cancelReturnResponseDao.getDetailResponseDaos()).hasSize(2)
                .extracting("productId")
                .contains(1L, 2L);
    }

    @Test
    @DisplayName("주문 상세 반환 리스트가 주어지지 않으면 에러를 발생시킨다.")
    public void changeDetailResponseDaosWithError() {
        // given
        CancelReturnResponseDao cancelReturnResponseDao = CancelReturnResponseDao.builder()
                .build();

        // when // then
        assertThatThrownBy(() -> cancelReturnResponseDao.changeDetailResponsDaos(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주어진 주문 상세 리스트가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주어진 주문 상세 반환 리스트가 빈 값 일 수 있다.")
    public void changeDetailResponseDaosWithEmptyList() {
        // given
        CancelReturnResponseDao cancelReturnResponseDao = CancelReturnResponseDao.builder()
                .build();
        List<DetailResponseDao> list = List.of();

        // when
        cancelReturnResponseDao.changeDetailResponsDaos(list);

        // then
        assertThat(cancelReturnResponseDao.getDetailResponseDaos()).hasSize(0);
    }

}