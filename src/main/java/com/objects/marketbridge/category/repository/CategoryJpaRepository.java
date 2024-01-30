package com.objects.marketbridge.category.repository;

import com.objects.marketbridge.common.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

    Category findByName(String name);

    Boolean existsByNameAndLevel(String name, Long level);

    Category findByNameAndLevel(String name, Long level);

    List<Category> findAllByNameAndLevel(String name, Long level);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByNameAndParentId(String name, Long parentId);
}