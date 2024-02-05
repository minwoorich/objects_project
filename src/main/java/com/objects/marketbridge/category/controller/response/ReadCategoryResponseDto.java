package com.objects.marketbridge.category.controller.response;

import com.objects.marketbridge.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ReadCategoryResponseDto {

    private Long id;
    private Long parentId;
    private Long level; //대분류 0L, 중분류 1L, 소분류 2L.
    private String name;
    private List<?> childCategories;

    @Builder
    public ReadCategoryResponseDto(Long id, Long parentId, Long level, String name, List<Category> childCategories) {
        this.id = id;
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.childCategories = childCategories;
    }
}
