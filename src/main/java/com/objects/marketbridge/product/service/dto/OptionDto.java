package com.objects.marketbridge.product.service.dto;

import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
public class OptionDto {

    private String optionCategory;
    private String name;

    @Builder
    public OptionDto(String optionCategory, String name) {
        this.optionCategory = optionCategory;
        this.name = name;
    }

    public OptionDto create(String optionCategory, String name){
        return OptionDto.builder()
                .optionCategory(optionCategory)
                .name(name)
                .build();
    }
}
