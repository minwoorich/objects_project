package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.ProductImage;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductImageRepositoryImpl implements ProductImageRepository {

    private final ProductImageJpaRepository productImageJpaRepository;

    @Override
    public void save(ProductImage productImage) {
        productImageJpaRepository.save(productImage);
    }

}
