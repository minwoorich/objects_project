package com.objects.marketbridge.domain.product.repository;

import com.objects.marketbridge.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductReposityImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

//    ProductJpaRepository가 이미구현체를 제공하기때문에 따로 만들어줄 필요가 없다.
//    @Override
//    public Optional<Product> findById(Long productId) {
//        return Optional.empty();
//    }

    public void save(Product product) {
        productJpaRepository.save(product);
    }


}
