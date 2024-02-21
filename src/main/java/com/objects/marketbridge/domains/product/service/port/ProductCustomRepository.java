package com.objects.marketbridge.domains.product.service.port;

import com.objects.marketbridge.domains.product.domain.Product;

public interface ProductCustomRepository {

    Product findByIdwithCategory(Long id);
}
