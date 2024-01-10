package com.objects.marketbridge.domain.product.service;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.product.dto.ProductDto;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void create() {
        Product product = Product.builder().build();
        productRepository.save(product);
    }

    public void getProductList(){

    }
}
