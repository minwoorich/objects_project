package com.objects.marketbridge.category.controller;

import com.objects.marketbridge.category.controller.response.ReadCategoryResponseDto;
import com.objects.marketbridge.category.service.CategoryService;
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

    @PostMapping("/categories/uploadExcel")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        return categoryService.uploadExcelFile(file);
    }

    //전체(라지,미디엄,스몰). 라지가 해당 미디엄 전부를, 미디엄이 해당 스몰 전부를 포함하는 형태로 JSON형식.
    @GetMapping("/categories/getTotalCategories")
    public List<ReadCategoryResponseDto> getTotalCategories() {
        return categoryService.getTotalCategories();
    }

    //특정부모카테고리(라지)의 미디엄(스몰 포함) 전체.
    @GetMapping("/categories/get2DepthCategories/{parentId}")
    public List<ReadCategoryResponseDto> get2DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get2DepthCategories(parentId);
    }

    //특정부모카테고리(미디엄)의 스몰 전체.
    @GetMapping("/categories/get3DepthCategories/{parentId}")
    public List<ReadCategoryResponseDto> get3DepthCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.get3DepthCategories(parentId);
    }

}
