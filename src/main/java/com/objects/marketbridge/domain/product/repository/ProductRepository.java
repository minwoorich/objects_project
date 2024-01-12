package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAllById(List<Long> ids);
    void save(Product product);

    void saveAll(List<Product> products);
}
