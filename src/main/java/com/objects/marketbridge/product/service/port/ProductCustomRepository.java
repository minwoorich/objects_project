package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.dto.OptionDto;

import java.util.List;

public interface ProductCustomRepository {

    Product findByIdwithCategory(Long id);

    List<Product> findAllByProductNoLikeAndProductId(String productNo, Long productId);
}
