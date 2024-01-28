package com.objects.marketbridge.product.service.port;

import com.objects.marketbridge.common.domain.Image;

public interface ImageRepository {

    void save(Image image);

    Image findById(Long id);
}
