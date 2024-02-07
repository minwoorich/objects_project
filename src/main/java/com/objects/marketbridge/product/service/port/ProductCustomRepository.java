package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.dto.ProductSearchConditionDto;
import com.objects.marketbridge.product.dto.ProductSimpleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

    Page<ProductSimpleDto> findAllProductWithCondition(ProductSearchConditionDto conditionDto, Pageable pageable);
}
