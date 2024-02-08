package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.TagCategory;

public interface TagCategoryRepository {
    TagCategory findByName(String name);
    void save(TagCategory tagCategory);
}
