package com.objects.marketbridge.category.controller;

import com.objects.marketbridge.category.controller.request.DeleteCategoryRequestDto;
import com.objects.marketbridge.category.controller.request.UpdateCategoryRequestDto;
import com.objects.marketbridge.category.controller.response.CreateCategoryResponseDto;
import com.objects.marketbridge.category.controller.response.DeleteCategoryResponseDto;
import com.objects.marketbridge.category.controller.response.ReadCategoryResponseDto;
import com.objects.marketbridge.category.controller.response.UpdateCategoryResponseDto;
import com.objects.marketbridge.category.service.*;
import com.objects.marketbridge.common.interceptor.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final BulkUploadCategoryService bulkUploadCategoryService;
    private final CreateCategoryService createCategoryService;
    private final ReadCategoryService readCategoryService;
    private final UpdateCategoryService updateCategoryService;
    private final DeleteCategoryService deleteCategoryService;


    //벌크 등록
    @UserAuthorize
    @PostMapping("/categories/uploadExcel")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        return bulkUploadCategoryService.uploadExcelFile(file);
    }



//    //단일 등록
//    @UserAuthorize
//    @PostMapping("/categories")
//    public ApiResponse<CreateCategoryResponseDto> createCategory(@Valid @RequestBody CreateReviewRequestDto request){
//        Long categoryId = createCategoryService.create(request);
//        CreateCategoryResponseDto response = new CreateCategoryResponseDto(categoryId);
//        return ApiResponse.ok(response);
//    }
//


    //조회
    //전체(라지,미디엄,스몰). 라지가 해당 미디엄 전부를, 미디엄이 해당 스몰 전부를 포함하는 형태로 JSON형식.
    @UserAuthorize
    @GetMapping("/categories/getTotalCategories")
    public List<ReadCategoryResponseDto> getTotalCategories() {
        return readCategoryService.getTotalCategories();
    }
    //특정부모카테고리(라지)의 미디엄(스몰 포함) 전체.
    @UserAuthorize
    @GetMapping("/categories/get2DepthCategories/{parentId}")
    public List<ReadCategoryResponseDto> get2DepthCategories(@PathVariable("parentId") Long parentId) {
        return readCategoryService.get2DepthCategories(parentId);
    }
    //특정부모카테고리(미디엄)의 스몰 전체.
    @UserAuthorize
    @GetMapping("/categories/get3DepthCategories/{parentId}")
    public List<ReadCategoryResponseDto> get3DepthCategories(@PathVariable("parentId") Long parentId) {
        return readCategoryService.get3DepthCategories(parentId);
    }



//    //단일 수정 //부모카테고리-자식카테고리 때문에 로직이 복잡해질듯하다...
//    @UserAuthorize
//    @PatchMapping("/categories/{id}")
//    public ApiResponse<UpdateCategoryResponseDto> updateCategory
//        (@PathVariable("id") Long id, @Valid @RequestBody UpdateCategoryRequestDto request) {
//        UpdateCategoryResponseDto response = updateCategoryService.update(id, request);
//        return ApiResponse.ok(response);
//    }



//    //단일 삭제 //부모카테고리-자식카테고리 때문에 로직이 복잡해질듯하다...
//    @UserAuthorize
//    @DeleteMapping("/categories/{id}")
//    public ApiResponse<DeleteCategoryResponseDto> deleteCategory
//        (@PathVariable("id") Long id, @Valid @RequestBody DeleteCategoryRequestDto request){
//        DeleteCategoryResponseDto response = deleteCategoryService.delete(id, request);
//        return ApiResponse.ok(response);
//    }

}
