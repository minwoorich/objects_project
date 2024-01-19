package com.objects.marketbridge.domain.product.dto;

import com.objects.marketbridge.domain.model.Image;
import com.objects.marketbridge.domain.model.Option;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductResponseDto {

    private Long productId;

    @Builder
    //에러나서 기본생성자 임시로 만듦.
    public ProductResponseDto() {
    }

    @Builder
    public ProductResponseDto(Long productId) {
        this.productId = productId;
    }


}
