package com.objects.marketbridge.image.infra;

import com.objects.marketbridge.image.domain.Image;

public interface ImageRepository {

    void save(Image image);

    Image findById(Long id);

    void delete(Image image);

    void deleteById(Long id);
}
