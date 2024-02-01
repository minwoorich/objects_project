package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.common.domain.Image;

import java.util.Optional;

public interface ImageRepository {

    void save(Image image);

    Optional<Image> findById(Long id);

    void delete(Image image);

    void deleteById(Long id);
}
