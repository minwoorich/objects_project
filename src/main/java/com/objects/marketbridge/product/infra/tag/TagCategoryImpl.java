package com.objects.marketbridge.product.infra.tag;

import com.objects.marketbridge.product.domain.TagCategory;
import com.objects.marketbridge.product.service.port.TagCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagCategoryImpl implements TagCategoryRepository {
    private final TagCategoryJpaRepository tagCategoryJpaRepository;

    @Override
    public TagCategory findByName(String name) {
        return tagCategoryJpaRepository.findByName(name).orElseGet(() -> TagCategory.builder().name("EMPTY").build());
    }

    @Override
    public void save(TagCategory tagCategory) {
        tagCategoryJpaRepository.save(tagCategory);
    }
}
