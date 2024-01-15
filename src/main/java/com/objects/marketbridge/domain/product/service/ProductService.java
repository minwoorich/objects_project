package com.objects.marketbridge.domain.product.service;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.product.dto.ProductDto;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryImpl productRepositoryImpl;

    public Product registerProduct(ProductDto productDto) {
//        Product product = Product.builder().build();
//        productRepository.save(product);

        // ProductDto에서 필요한 정보 추출하여 Product 엔터티 생성
        Product product = Product.builder()
                .categoryId(productDto.getCategoryId())
                .isOwn(productDto.isOwn())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .isSubs(productDto.isSubs())
                .thumbImg(productDto.getThumbImg())
                .discountRate(productDto.getDiscountRate())
                .build();

        // ProductRepositoryImpl 통해 엔터티를 저장
        return productRepositoryImpl.save(product);

    }

    public void getProductList(){

    }
}
