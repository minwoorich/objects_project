package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DeleteProductServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DeleteProductService deleteProductService;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("상품을_삭제하면_삭제된다")

        //given
    void updateProductTest(){
//        Category category = Category.builder()
//                .parentId(null)
//                .level(0L)
//                .name("대분류카테고리1")
//                .build();
//        categoryRepository.save(category);
//
//        Product product = Product.builder()
//                .category(category)
//                .discountRate(30L)
//                .isOwn(false)
//                .isSubs(false)
//                .productNo("pn0003")
//                .price(5000L)
//                .stock(100L)
//                .thumbImg("thumbImgTest1.jpg")
//                .name("테스트용상품1")
//                .build();
//
//        productRepository.save(product);
//        em.flush();
//        em.clear();
//
//
//        //when
//
//        deleteProductService.delete(product.getId());
//
//        //then
//        System.out.println("product = " + product);
//        System.out.println("product.getId() = " + product.getId());
//
//        Assertions.assertThatThrownBy(() -> productRepository.findById(product.getId()))
//                .isInstanceOf(JpaObjectRetrievalFailureException.class);

    }
}