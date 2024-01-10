package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;

import java.util.Optional;

public interface ProductRepository{

//    ProductJpaRepository가 이미구현체를 제공하기때문에 따로 만들어줄 필요가 없다.
//
//    Optional<Product> findById(Long productId);

    void save(Product product);
}
