package com.objects.marketbridge.domains.product.service;

import com.objects.marketbridge.domains.category.domain.Category;
import com.objects.marketbridge.domains.category.service.port.CategoryRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.dto.CreateProductDto;
import com.objects.marketbridge.domains.product.service.ProductService;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;



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
        Product product = productService.createProduct(createProductDto);
        Assertions.assertThat(product.getCategory().getLevel()).isEqualTo(0L);
    }
}