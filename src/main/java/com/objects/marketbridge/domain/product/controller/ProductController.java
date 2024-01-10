package com.objects.marketbridge.domain.product.controller;

import com.objects.marketbridge.domain.product.dto.ProductRequestDto;
import com.objects.marketbridge.domain.product.dto.ProductResponseDto;
import com.objects.marketbridge.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping("/products")
    public ProductResponseDto getProductList(@Valid @RequestBody ProductRequestDto request){
        // 담을거 가져오기
        return new ProductResponseDto();
    }
}
