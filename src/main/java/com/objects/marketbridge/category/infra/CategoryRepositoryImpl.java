package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.category.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    @Override
    public Category findById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public void save(Category category) {
        categoryJpaRepository.save(category);
    }

    @Override
    public void saveAll(List<Category> categories) {
        categoryJpaRepository.saveAll(categories);
    }

    @Override
    public Category findByName(String name) {
        return categoryJpaRepository.findByName(name).orElseGet(() -> Category.builder().name("EMPTY").build());
    }

    @Override
    public List<Category> findAllByLevelAndParentIdIsNull(Long level) {
        return categoryJpaRepository.findAllByLevelAndParentIdIsNull(level).orElseGet(ArrayList::new);
    }

    @Override
    public List<Category> findAllByLevelAndParentId(Long level, Long parentId) {
        // null일 때 빈 배열 반환
        return categoryJpaRepository.findAllByLevelAndParentId(level, parentId).orElseGet(ArrayList::new);
    }

    @Override
    public List<Category> findAllByParentId(Long parentId) {
        return categoryJpaRepository.findAllByParentId(parentId).orElseGet(ArrayList::new);
    }

}
