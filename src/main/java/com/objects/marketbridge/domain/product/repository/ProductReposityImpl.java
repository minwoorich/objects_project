package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductReposityImpl implements ProductRepository {

    @Override
    public Optional<Product> findById(Long productId) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        return null;
    }
}
