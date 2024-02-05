package com.objects.marketbridge.category.controller.response;

import lombok.Builder;

public class CreateCategoryResponseDto {

    private Long categoryId;

    @Builder
    public CreateCategoryResponseDto(Long categoryId) {
        this.categoryId = categoryId;
    }
}
