package com.objects.marketbridge.domain.product.service.port;

import com.objects.marketbridge.common.domain.Category;

public interface CategoryRepository {

    Category findById(Long id);

}
