package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.common.domain.Option;

public interface OptionRepository {

    void save(Option option);

    Option findById(Long id);

    Option findByName(String name);
}
