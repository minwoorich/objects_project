package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.common.domain.ProductImage;

import java.util.List;

public interface ProductImageRepository {

    void save(ProductImage productImage);

    public List<ProductImage> findAllByProductId(Long productId);

    void delete(ProductImage productImage);

    void saveAll(List<ProductImage> productImages);
}
