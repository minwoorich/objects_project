package com.objects.marketbridge.domain.product.service;

import com.objects.marketbridge.domain.model.Stock;
import com.objects.marketbridge.domain.product.repository.stock.StockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;
    
    // TODO stock 제약조건으로 테스트 안됨 추가적인 코드 수정 필요
    @Test
    @DisplayName("동시 요청이 들어와도 성공해야 한다.")
    public void decrease() throws InterruptedException {
        // given
        Stock stock = Stock.builder()
                .quantity(100L)
                .build();
        Stock savedStock = stockRepository.save(stock);
        Long stockId = savedStock.getId();

        int count = 100;
        int threads = 32;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(count);

        // when
        for (int i = 0; i < count; i++) {
            executorService.submit(() -> {
                try{
                    stockService.decrease(stockId, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Assertions.assertThat(0L).isEqualTo(savedStock.getQuantity());
    }

}