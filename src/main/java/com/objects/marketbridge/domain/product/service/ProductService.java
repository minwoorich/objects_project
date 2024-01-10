package com.objects.marketbridge.domain.product.service;

import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class ProductService {
    private ProductRepository productRepository;

    public void getProductList(){

    }
}
