package com.objects.marketbridge.domain.product.repository.warehouse;

import com.objects.marketbridge.domain.model.Warehouse;

import java.util.List;

public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

    void saveAll(List<Warehouse> warehouses);

}
