package com.objects.marketbridge.category.controller.response;

import lombok.Builder;

public class CategoryCreateResponseDto {

    private Long categoryId;

    @Builder
    public CategoryCreateResponseDto(Long categoryId) {
        this.categoryId = categoryId;
    }
}
