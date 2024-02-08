package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import com.objects.marketbridge.product.dto.UpdateProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UpdateProductServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UpdateProductService updateProductService;

    @Test
    @DisplayName("상품정보를_수정하면_수정된다")

    //given
    void updateProductTest(){
        Category category = Category.builder()
                .parentId(null)
                .level(0L)
                .name("대분류카테고리1")
                .build();
        categoryRepository.save(category);

        Product product = Product.builder()
                .category(category)
                .discountRate(30L)
                .isOwn(false)
                .isSubs(false)
                .productNo("pn0003")
                .price(5000L)
                .stock(100L)
                .thumbImg("thumbImgTest1.jpg")
                .name("테스트용상품1")
                .build();

        productRepository.save(product);

        //when
        UpdateProductDto updateProductDto = UpdateProductDto.builder()
                .categoryId(category.getId())
                .discountRate(33L)
                .isOwn(true)
                .isSubs(true)
                .productNo("pn3333")
                .price(5555L)
                .stock(111L)
                .thumbImg("thumbImgTest11.jpg")
                .name("테스트용상품11")
                .build();

        Product productUpdated
                = updateProductService.updateProduct(product, updateProductDto);

        //then
        Assertions.assertThat(productUpdated.getProductNo()).isEqualTo("pn3333");
        Assertions.assertThat(product.getId()).isEqualTo(productUpdated.getId());
        Assertions.assertThat(product).isEqualTo(productUpdated);
    }
}
