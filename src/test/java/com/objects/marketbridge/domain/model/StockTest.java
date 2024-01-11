package com.objects.marketbridge.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StockTest {

    @Test
    @DisplayName("재고가 감소해야 한다.")
    public void decrease() {
        // given
        Stock stock = createStock(100L);

        // when
        stock.decrease(1L);

        // then
        assertThat(stock.getQuantity()).isEqualTo(99);
    }


    @Test
    @DisplayName("재고가 부족하면 에러가 난다.")
    public void decreaseError() {
        // given
        Stock stock = createStock(100L);

        // when // then
        assertThatThrownBy(() -> stock.decrease(101L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고는 0개 미만이 될 수 없습니다.");
    }

    private static Stock createStock(long quantity) {
        return Stock.builder().quantity(quantity).build();
    }




}