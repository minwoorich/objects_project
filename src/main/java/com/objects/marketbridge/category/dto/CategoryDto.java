package com.objects.marketbridge.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;
    private Long parentId;
    private Long level;
    private String name;
    private List<CategoryDto> chidCategories = new ArrayList<>();

    @Builder
    public CategoryDto(Long categoryId,Long parentId, Long level, String name) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.level = level;
        this.name = name;
    }

    public void addChildCategories(CategoryDto categoryDto){
        chidCategories.add(categoryDto);
    }

    public void addChildCategories(List<CategoryDto> categoryDto){
        chidCategories.addAll(categoryDto);
    }
}
