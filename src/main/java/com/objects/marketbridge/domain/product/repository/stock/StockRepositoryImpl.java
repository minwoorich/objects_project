package com.objects.marketbridge.domain.product.repository.stock;


import com.objects.marketbridge.domain.model.Stock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.domain.model.QProdOption.prodOption;
import static com.objects.marketbridge.domain.model.QProduct.product;
import static com.objects.marketbridge.domain.model.QStock.stock;
import static com.objects.marketbridge.domain.order.domain.QProdOrder.prodOrder;
import static com.objects.marketbridge.domain.order.domain.QProdOrderDetail.prodOrderDetail;

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

    @Override
    public List<Stock> findStockByProdOrderId(Long prodOrderId) {
        return null;
    }

//    @Override
//    public List<Stock> findStockByProdOrderId(Long prodOrderId) {
//        return queryFactory
//                .selectFrom(stock)
//                .join(stock.productOption, prodOption)
//                .join(prodOption.product, product)
//                .join(product.prodOrderDetails, prodOrderDetail)
//                .join(prodOrderDetail.prodOrder, prodOrder)
//                .where(prodOrder.id.eq(prodOrderId))
//                .fetch();
//    }
}
