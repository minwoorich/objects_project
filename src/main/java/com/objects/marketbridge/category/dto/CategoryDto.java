package com.objects.marketbridge.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CategoryDto {

    private Long categoryId;
    private Long parentId;
    private Long level;
    private String name;
    private List<CategoryDto> childCategories = new ArrayList<>();

    @Builder
    public CategoryDto(Long categoryId, Long parentId, Long level, String name, List<CategoryDto> childCategories) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.childCategories = childCategories;
    }

    public void addChildCategories(CategoryDto categoryDto){
        childCategories.add(categoryDto);
    }

    public void addChildCategories(List<CategoryDto> categoryDto){
        childCategories.addAll(categoryDto);
    }
}