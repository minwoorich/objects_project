package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.Option;

import java.util.List;

public interface OptionRepository {
    void save(Option option);

    Option findById(Long id);

    Option findByName(String name);

    Option findByNameAndOptionCategoryId(String name, Long optionCategoryId);
    void deleteAllInBatch();

    void saveAll(List<Option> options);
}
