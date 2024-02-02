package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product findById(Long id);

    List<Product> findAllById(List<Long> ids);

    List<Product> findByName(String name);

    void deleteAllInBatch();
    Product save(Product product);

    List<Product> findAll();

    void saveAll(List<Product> products);

    void delete(Product product);
}
