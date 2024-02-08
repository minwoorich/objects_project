package com.objects.marketbridge.category.controller;

import com.objects.marketbridge.category.dto.CategoryDto;
import com.objects.marketbridge.category.service.*;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //전체(라지,미디엄,스몰). 라지가 해당 미디엄 전부를, 미디엄이 해당 스몰 전부를 포함하는 형태로 JSON형식.
    @UserAuthorize
    @GetMapping("/categories/getTotalCategories")
    public List<CategoryDto> getTotalCategories() {
        return categoryService.getTotalCategories();
    }
    //특정부모카테고리(라지)의 미디엄(스몰 포함) 전체.
    @UserAuthorize
    @GetMapping("/categories/get2DepthCategories/{parentId}")
    public List<CategoryDto> get2DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get2DepthCategories(parentId);
    }
    //특정부모카테고리(미디엄)의 스몰 전체.
    @UserAuthorize
    @GetMapping("/categories/get3DepthCategories/{parentId}")
    public List<CategoryDto> get3DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get3DepthCategories(parentId);
    }

}
