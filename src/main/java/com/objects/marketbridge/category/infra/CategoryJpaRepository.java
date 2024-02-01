package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.common.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    Boolean existsByName(String name);

    Optional<Category> findByName(String name);

    Boolean existsByNameAndLevel(String name, Long level);

    Optional<Category> findByNameAndLevel(String name, Long level);

    List<Category> findAllByNameAndLevel(String name, Long level);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByNameAndParentId(String name, Long parentId);
}