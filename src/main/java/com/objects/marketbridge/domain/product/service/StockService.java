package com.objects.marketbridge.domain.product.service;

import com.objects.marketbridge.domain.model.Stock;
import com.objects.marketbridge.domain.product.repository.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findByIdWithLock(id);

        stock.decrease(quantity);

        stockRepository.save(stock);
    }

}
