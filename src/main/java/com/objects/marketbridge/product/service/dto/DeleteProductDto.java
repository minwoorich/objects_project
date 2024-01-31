package com.objects.marketbridge.product.service.dto;

import com.objects.marketbridge.product.controller.request.DeleteProductRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Component
public class DeleteProductDto {

    private Long productId;

    @Builder
    public DeleteProductDto(Long productId) {
        this.productId = productId;
    }

    public static DeleteProductDto fromRequest(DeleteProductRequestDto request){
        return DeleteProductDto.builder()
                .productId(request.getProductId())
                .build();
    }
}
