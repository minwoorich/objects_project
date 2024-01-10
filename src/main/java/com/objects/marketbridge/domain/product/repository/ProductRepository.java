package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long productId);

    Product save(Product product);
}
