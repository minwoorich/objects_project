//package com.objects.marketbridge.product.service;
//
//import com.objects.marketbridge.category.dto.CategoryDto;
//import com.objects.marketbridge.category.service.CategoryService;
//import com.objects.marketbridge.product.dto.ProductSearchConditionDto;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
////@ActiveProfiles("test")
//@Transactional
//class GetProductServiceTest {
//
//    @Autowired GetProductService getProductService;
//    @Autowired CategoryService categoryService;
//
//    @Test
//    @DisplayName("3DEPTH만_뽑아낼_수_있다")
//    void filterTo3DepthTest(){
//        List<CategoryDto> categoryDtos = categoryService.getLowerCategories(9L);
//        ProductSearchConditionDto resultDto = ProductSearchConditionDto.builder().build();
//        getProductService.filterTo3Depth(categoryDtos,resultDto);
//        System.out.println("resultDto.getLeafDepthInfoList().size() = " + resultDto.getLeafDepthInfoList().size());
////        Assertions.assertThat(targetCategoryList.get(0).getLevel()).isEqualTo(3L);
//    }
//}