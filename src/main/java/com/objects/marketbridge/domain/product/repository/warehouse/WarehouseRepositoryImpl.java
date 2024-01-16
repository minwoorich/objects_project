package com.objects.marketbridge.domain.product.repository.warehouse;

import com.objects.marketbridge.domain.model.QWarehouse;
import com.objects.marketbridge.domain.model.Warehouse;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.objects.marketbridge.domain.model.QWarehouse.*;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository{
    private final WarehouseJpaRepository warehouseJpaRepository;
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public WarehouseRepositoryImpl(WarehouseJpaRepository warehouseJpaRepository, EntityManager em) {
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        return warehouseJpaRepository.save(warehouse);
    }

    @Override
    public void saveAll(List<Warehouse> warehouses) {
        warehouseJpaRepository.saveAll(warehouses);
    }
}
