package com.objects.marketbridge.category.service.port;

import com.objects.marketbridge.common.domain.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    Boolean existsByName(String name);

    void save(Category category);

    Optional<Category> findByName(String name);

    Boolean existsByNameAndLevel(String name, Long level);

    Optional<Category> findByNameAndLevel(String name, Long level);

    List<Category> findAllByNameAndLevel(String name, Long level);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByNameAndParentId(String name, Long parentId);
}
