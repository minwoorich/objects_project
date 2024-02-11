package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.product.domain.Tag;

import java.util.Optional;

public interface TagRepository {
    Tag findByName(String name);

    void save(Tag tag);

    Tag findById(Long id);
}
