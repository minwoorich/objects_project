package com.objects.marketbridge.product.controller;

import com.objects.marketbridge.product.controller.request.ProductCreateRequestDto;
import com.objects.marketbridge.product.controller.request.ProductUpdateRequestDto;
import com.objects.marketbridge.product.controller.response.ProductCreateResponseDto;
import com.objects.marketbridge.product.controller.response.ProductDeleteResponseDto;
import com.objects.marketbridge.product.controller.response.ProductReadResponseDto;
import com.objects.marketbridge.product.controller.response.ProductUpdateResponseDto;
import com.objects.marketbridge.product.service.ProductService;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



    //상품들 Excel파일로 대량등록
    @UserAuthorize
    @PostMapping("/products/uploadExcel")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        return productService.uploadExcelFile(file);
    }



    //상품등록
    @UserAuthorize
    @PostMapping("/products")
    public ApiResponse<ProductCreateResponseDto> createProduct
    (@Valid @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        Long productId = productService.createProduct(productCreateRequestDto);
        ProductCreateResponseDto createProductResponseDto = new ProductCreateResponseDto(productId);
        return ApiResponse.ok(createProductResponseDto);
    }



    //상품조회
    @UserAuthorize
    @GetMapping("/products/{id}")
    public ApiResponse<ProductReadResponseDto> readProduct
    (@PathVariable("id") Long id){
        ProductReadResponseDto productReadResponseDto = productService.readProduct(id);
        return ApiResponse.ok(productReadResponseDto);
    }



    //상품수정
    @UserAuthorize
    @PatchMapping("/products/{id}")
    public ApiResponse<ProductUpdateResponseDto> updateProduct
    (@PathVariable("id") Long id, @RequestBody @Valid ProductUpdateRequestDto updateProductRequestDto) {
        ProductUpdateResponseDto productUpdateResponseDto
                = productService.updateProduct(id, updateProductRequestDto);
        return ApiResponse.ok(productUpdateResponseDto);
    }



    //상품삭제
    @UserAuthorize
    @DeleteMapping("/products/{id}")
    public ApiResponse<ProductDeleteResponseDto> deleteProduct
    (@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        ProductDeleteResponseDto productDeleteResponseDto = new ProductDeleteResponseDto();
        return ApiResponse.ok(productDeleteResponseDto);
    }



}
