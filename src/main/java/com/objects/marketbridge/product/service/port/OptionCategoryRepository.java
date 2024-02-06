package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.OptionCategory;

public interface OptionCategoryRepository {
    OptionCategory findByName(String name);

    void save(OptionCategory optionCategory);

    OptionCategory findById(Long id);
}
