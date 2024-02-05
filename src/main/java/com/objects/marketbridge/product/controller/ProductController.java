package com.objects.marketbridge.product.controller;

import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import com.objects.marketbridge.product.controller.request.DeleteProductRequestDto;
import com.objects.marketbridge.product.controller.request.UpdateProductRequestDto;
import com.objects.marketbridge.product.controller.response.CreateProductResponseDto;
import com.objects.marketbridge.product.controller.response.DeleteProductResponseDto;
import com.objects.marketbridge.product.controller.response.ReadProductResponseDto;
import com.objects.marketbridge.product.controller.response.UpdateProductResponseDto;
import com.objects.marketbridge.product.service.*;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.product.service.dto.OptionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final BulkUploadProductService bulkUploadProductService;
    private final CreateProductService createProductService;
    private final ReadProductService readProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;


    //상품들 Excel파일로 대량등록
    @UserAuthorize
    @PostMapping("/uploadExcel")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        return bulkUploadProductService.uploadExcelFile(file);
    }


    //상품등록
    @UserAuthorize
    @PostMapping()
    public ApiResponse<CreateProductResponseDto> createProduct
    (@Valid @RequestBody CreateProductRequestDto request) {
        Long productId = createProductService.create(request);
        CreateProductResponseDto createProductResponseDto = new CreateProductResponseDto(productId);
        return ApiResponse.ok(createProductResponseDto);
    }


    //상품조회
    @UserAuthorize
    @GetMapping("/{id}")
    public ApiResponse<ReadProductResponseDto> readProduct
    (@PathVariable("id") Long id){
        ReadProductResponseDto readProductResponseDto = readProductService.read(id);
        return ApiResponse.ok(readProductResponseDto);
    }


    //상품수정
    @UserAuthorize
    @PatchMapping("/{id}")
    public ApiResponse<UpdateProductResponseDto> updateProduct
    (@PathVariable("id") Long id, @RequestBody @Valid UpdateProductRequestDto updateProductRequestDto) {
        UpdateProductResponseDto updateProductResponseDto
                = updateProductService.update(updateProductRequestDto);
        return ApiResponse.ok(updateProductResponseDto);
    }


    //상품삭제
    @UserAuthorize
    @DeleteMapping("/{id}")
    public ApiResponse<DeleteProductResponseDto> deleteProduct
    (@PathVariable("id") Long id, @RequestBody @Valid DeleteProductRequestDto deleteProductRequestDto) {
        DeleteProductResponseDto deleteProductResponseDto = deleteProductService.delete(deleteProductRequestDto);
        return ApiResponse.ok(deleteProductResponseDto);
    }
}
