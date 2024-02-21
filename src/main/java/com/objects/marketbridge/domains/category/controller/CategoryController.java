package com.objects.marketbridge.domains.category.controller;

import com.objects.marketbridge.domains.category.dto.CategoryDto;
import com.objects.marketbridge.category.service.*;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.domains.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;
//    private final BulkUploadCategoryService bulkUploadCategoryService;


//    //벌크 등록
//    @UserAuthorize
//    @PostMapping("/categories/uploadExcel")
//    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
//        return bulkUploadCategoryService.uploadExcelFile(file);
//    }


    //조회
    //전체(1depth,2depth,3depth). 1depth가 하위의 2depth 전부를, 2depth 카테고리가 하위의 3depth 전부를 포함하는 형태로 JSON형식.
    @UserAuthorize
    @GetMapping("/categories/getTotalCategories")
    public List<CategoryDto> getTotalCategories() {
        return categoryService.getTotalCategories();
    }

    //특정부모카테고리(1depth)의 2depth(3depth 포함) 전체.
    @UserAuthorize
    @GetMapping("/categories/get2DepthCategories/{parentId}")
    public List<CategoryDto> get2DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get2DepthCategories(parentId);
    }

    //특정부모카테고리(2depth)의 3depth 전체.
    @UserAuthorize
    @GetMapping("/categories/get3DepthCategories/{parentId}")
    public List<CategoryDto> get3DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get3DepthCategories(parentId);
    }

    // 하위 카테고리 전체 조회
    @UserAuthorize
    @GetMapping("/categories")
    public List<CategoryDto> getLowerCategories(@RequestParam("categoryId") Long categoryId) {
        return categoryService.getLowerCategories(categoryId);
    }
}
