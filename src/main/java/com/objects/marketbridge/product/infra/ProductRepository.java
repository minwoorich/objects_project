package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.product.domain.Product;

import java.util.List;

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
