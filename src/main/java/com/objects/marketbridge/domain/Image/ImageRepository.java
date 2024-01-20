package com.objects.marketbridge.domain.Image;

import com.objects.marketbridge.domain.model.Image;

public interface ImageRepository {

    void save(Image image);

    Image findById(Long id);
}
