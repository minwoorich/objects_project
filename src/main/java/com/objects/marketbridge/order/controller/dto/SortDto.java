package com.objects.marketbridge.order.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SortDto {
    private String property;
    private String direction;

    @Builder
    private SortDto(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }
}
