package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    Boolean existsByName(String name);

    Boolean existsByNameAndLevel(String name, Long level);

    Category findByNameAndLevel(String name, Long level);

    List<Category> findAllByNameAndLevel(String name, Long level);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByNameAndParentId(String name, Long parentId);
}