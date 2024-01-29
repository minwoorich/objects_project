package com.objects.marketbridge.category.controller;

import com.objects.marketbridge.category.controller.response.CategoryReadResponseDto;
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

//    @GetMapping("/categories/all")
//    public List<Category> getAllCategories() {
//        return categoryService.getAllCategories();
//    }

    @GetMapping("/categories/largeCategories")
    public List<CategoryReadResponseDto> getLargeCategories() {
        return categoryService.getLargeCategories();
    }

    @GetMapping("/categories/mediumCategories/{parentId}")
    public List<CategoryReadResponseDto> getMediumCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.getMediumCategories(parentId);
    }

    @GetMapping("/categories/smallCategories/{parentId}")
    public List<CategoryReadResponseDto> getSmallCategories(@PathVariable("parentId") Long parentId) {
        return categoryService.getSmallCategories(parentId);
    }


}
