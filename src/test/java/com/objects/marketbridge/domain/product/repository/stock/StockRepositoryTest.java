package com.objects.marketbridge.domain.product.repository.stock;

import com.objects.marketbridge.domain.model.Stock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;


    @Test
    @DisplayName("재고를 저장할 수 있다.")
    public void save() {
        // given
        Stock stock = Stock.builder().quantity(10L).build();

        // when
        Stock savedStock = stockRepository.save(stock);

        // then
        assertThat(stock.getQuantity()).isEqualTo(savedStock.getQuantity());
    }

    // TODO
    @Test
    @DisplayName("db락을 설정하고 아이디로 재고를 조회 할 수 있다.")
    public void findByIdWithLock() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("아이디를 가지고 재고를 조회할 수 있다.")
    public void findById() {
        // given
        Stock stock = Stock.builder().build();
        Stock savedStock = stockRepository.save(stock);

        // when
        Optional<Stock> findStockOpt = stockRepository.findById(stock.getId());

        // then
        assertThat(findStockOpt.get()).isEqualTo(savedStock);
    }

    @Test
    @DisplayName("저장된 재고가 없으면 조회할 수 없다.")
    public void findById_Error() {
        // given
        Long noId = 1L;

        // when
        Optional<Stock> findStockOpt = stockRepository.findById(noId);

        // then
        assertThatThrownBy(findStockOpt::get)
                .isInstanceOf(NoSuchElementException.class);
    }
}