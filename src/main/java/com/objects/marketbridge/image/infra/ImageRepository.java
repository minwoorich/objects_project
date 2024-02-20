package com.objects.marketbridge.image.infra;

import com.objects.marketbridge.image.domain.Image;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository {

    void save(Image image);

    Image findById(Long id);

    void delete(Image image);

    void deleteById(Long id);

    void deleteAll(Iterable<? extends Image> images);

    List<Image> findByIds(List<Long> ids);

    void deleteByIdIn(List<Long> ids);
}
