package com.objects.marketbridge.category.service.port;

import com.objects.marketbridge.product.domain.Category;

import java.util.List;

public interface CategoryRepository {

    Category findById(Long id);

    Boolean existsByName(String name);

    void save(Category category);

    Category findByName(String name);

    Boolean existsByNameAndLevel(String name, Long level);

    Category findByNameAndLevel(String name, Long level);

    List<Category> findAllByNameAndLevel(String name, Long level);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByNameAndParentId(String name, Long parentId);
}
