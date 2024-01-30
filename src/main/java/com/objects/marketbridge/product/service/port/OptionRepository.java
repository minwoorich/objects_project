package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.common.domain.Option;

import java.util.Optional;

public interface OptionRepository {

    void save(Option option);

    Optional<Option> findById(Long id);

    Optional<Option> findByName(String name);
}
