package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import com.objects.marketbridge.product.dto.CreateProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CreateProductServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CreateProductService createProductService;

    @Test
    void 상품_엔티티를_만들_수_있다(){
        Category category = Category.builder()
                .parentId(null)
                .name("의류")
                .level(0L)
                .build();
        categoryRepository.save(category);

        CreateProductDto createProductDto = CreateProductDto.builder()
                .discountRate(14L)
                .isOwn(true)
                .isSubs(true)
                .productNo("dfdfaf")
                .price(3400L)
                .stock(200L)
                .thumbImg("Fdfdf")
                .name("반팔티")
                .categoryId(category.getId())
                .build();
        Product product = createProductService.createProduct(createProductDto);

        Assertions.assertThat(product.getCategory().getLevel()).isEqualTo(0L);
    }
}