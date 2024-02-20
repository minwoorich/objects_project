package com.objects.marketbridge.category.service.port;

import com.objects.marketbridge.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category findById(Long id);

    void save(Category category);

    void saveAll(List<Category> categories);

    Category findByName(String name);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByParentId(Long parentId);
}
