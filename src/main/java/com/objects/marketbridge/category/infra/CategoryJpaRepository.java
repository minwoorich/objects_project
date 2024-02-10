package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.category.domain.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

//    Optional<Category> findByName(String name);
//
//    Optional<Category> findById(Long categoryId);
//
//    Optional<List<Category>> findAllByLevelAndParentIdIsNull(Long level);
//
//    @Query(value = "SELECT c FROM Category c WHERE c.level=:level AND c.parentId=:parentId ")
//    Optional<List<Category>> findAllByLevelAndParentId(@Param("level") Long level, @Param("parentId") Long parentId);
//
//    @Query(value = "SELECT c FROM Category c WHERE c.parentId=:parentId ")
//    Optional<List<Category>> findAllByParentId(@Param("parentId") Long parentId);

    Category findByName(String name);

    Optional<Category> findById(Long id);

    List<Category> findAllByLevelAndParentIdIsNull(Long level);

    List<Category> findAllByLevelAndParentId(Long level, Long parentId);

    List<Category> findAllByParentId(Long parentId);
}