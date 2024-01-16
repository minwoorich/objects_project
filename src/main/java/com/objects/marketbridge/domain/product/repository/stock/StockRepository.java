package com.objects.marketbridge.domain.product.repository.stock;

import com.objects.marketbridge.domain.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository {
    Stock save(Stock stock);

    Stock findByIdWithLock(Long id);

    Optional<Stock> findById(long id);

    List<Stock> findStocksByProdOrderId(Long prodOrderId);

}
