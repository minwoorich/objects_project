package com.objects.marketbridge.domain.product.repository.stock;


import com.objects.marketbridge.domain.model.Stock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.domain.model.QProduct.product;
import static com.objects.marketbridge.domain.model.QStock.stock;
import static com.objects.marketbridge.domain.order.entity.QProdOrder.prodOrder;
import static com.objects.marketbridge.domain.order.entity.QProdOrderDetail.prodOrderDetail;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final StockJpaRepository stockJpaRepository;

    public StockRepositoryImpl(EntityManager em, StockJpaRepository stockJpaRepository) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.stockJpaRepository = stockJpaRepository;
    }

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

//    @Override
//    public List<Stock> findStocksByProdOrderIdAndWarehouseId(Long prodOrderId, Long warehouseId) {
//         return queryFactory
//                .selectFrom(stock)
//                .join(stock.product, product)
//                .join(product.prodOrderDetails, prodOrderDetail)
//                .join(prodOrderDetail.prodOrder, prodOrder)
//                .where(
//                        prodOrder.id.eq(prodOrderId),
//                        stock.warehouse.id.eq(warehouseId)
//                )
//                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
//                .fetch();
//    }


    @Override
    public List<Stock> saveAll(List<Stock> stocks) {
        return stockJpaRepository.saveAll(stocks);
    }
}
