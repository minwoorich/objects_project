package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.product.dto.ProductRequestDto;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAllById(List<Long> ids);

    List<Product> findByName(String name);

    void deleteAllInBatch();
    void save(Product product);

    void saveAll(List<Product> products);
}
