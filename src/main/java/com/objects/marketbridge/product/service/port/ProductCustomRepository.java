package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.Product;

public interface ProductCustomRepository {

    Product findByIdwithCategory(Long id);
}
