package com.objects.marketbridge.domain.product.infra;

import com.objects.marketbridge.common.domain.Category;
import com.objects.marketbridge.domain.product.service.port.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category findById(Long id) {
        return categoryJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
