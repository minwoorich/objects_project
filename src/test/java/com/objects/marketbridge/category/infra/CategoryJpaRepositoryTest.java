//package com.objects.marketbridge.category.infra;
//
//import com.objects.marketbridge.category.domain.Category;
//import com.objects.marketbridge.category.service.port.CategoryRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//class CategoryJpaRepositoryTest {
//    @Autowired CategoryJpaRepository categoryJpaRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Test
//    void Basic(){
//        //given
//        Optional<List<Category>> category = categoryJpaRepository.findAllByParentId(1L);
//        //then
//        System.out.println("category = " + category.isPresent());
//        System.out.println("category.stream().count() = " + category.stream().count());
//    }
//
//    @Test
//    void Basic2(){
//        //given
//        List<Category> categories = categoryRepository.findAllByParentId(1L);
//        //then
//        System.out.println("categories.size() = " + categories.size());
//    }
//}