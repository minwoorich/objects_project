package com.objects.marketbridge.domain.product.repository.stock;


import com.objects.marketbridge.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Stock save(Stock stock) {
        return stockJpaRepository.save(stock);
    }

    @Override
    public Stock findByIdWithLock(Long id) {
        return stockJpaRepository.findByIdWithLock(id);
    }

    @Override
    public Optional<Stock> findById(long id) {
        return stockJpaRepository.findById(id);
    }
}
