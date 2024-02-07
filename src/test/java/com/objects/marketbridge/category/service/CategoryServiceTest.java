package com.objects.marketbridge.category.service;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.dto.CategoryDto;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class CategoryServiceTest {
//    @Autowired CategoryService categoryService;
//    @Autowired CategoryRepository categoryRepository;
//
//
//    @Test
//    @DisplayName("하위_카테고리를_가져올_수_있다")
//    void getLowerCategoriesTest(){
//        List<CategoryDto> categoryDtos = categoryService.getLowerCategories(320L);
//
////        System.out.println("categoryDtos.get(0).getName() = " + categoryDtos.get(0).getName());
//        for (CategoryDto category : categoryDtos) {
//            System.out.println("category.getName() = " + category.getName());
//            for (CategoryDto innerDto: category.getChidCategories()) {
//                System.out.println("innerDto.getName() = " + innerDto.getName());
//            }
//        }
//        Assertions.assertThat(categoryDtos.get(0).getCategoryId()).isEqualTo(1L);
//    }
//
//}