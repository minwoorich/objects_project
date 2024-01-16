package com.objects.marketbridge.domain.product.repository.warehouse;

import com.objects.marketbridge.domain.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseJpaRepository extends JpaRepository<Warehouse, Long> {

    Warehouse save(Warehouse warehouse);


}
