package com.objects.marketbridge.domains.product.domain;

import com.objects.marketbridge.domains.category.domain.Category;
import com.objects.marketbridge.domains.order.mock.FakeProductRepository;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductToProductGroupServiceTest {

    ProductRepository productRepository = new FakeProductRepository();


    @AfterEach
    void clear() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("Product 에서 ProductGroup 로 엔티티를 변환한다.")
    @Test
    void convert() {

        // given
        ProductToProductGroupService productToProductGroupService = new ProductToProductGroupService();

        Category category = Category.builder().name("의류").level(1L).build();
        Product product1 = Product.builder().name("자전거1").price(1000L).isOwn(false).stock(100L).discountRate(10L).isSubs(false).build();
        product1.setCategory(category);

        Product product2 = Product.builder().name("자전거2").price(2000L).isOwn(false).stock(200L).discountRate(20L).isSubs(false).build();
        product2.setCategory(category);

        Product product3 = Product.builder().name("자전거3").price(3000L).isOwn(false).stock(300L).discountRate(30L).isSubs(false).build();
        product3.setCategory(category);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<ProductGroup> productGroups = productToProductGroupService.convert(productRepository.findAll());

        //then
        assertThat(productGroups).hasSize(3);
        assertThat(productGroups)
                .extracting(ProductGroup::getCategory, ProductGroup::getIsOwn, ProductGroup::getName, ProductGroup::getIsSubs, ProductGroup::getDiscountRate)
                .containsExactly(
                        Tuple.tuple(category, false, "자전거1", false, 10L),
                        Tuple.tuple(category, false, "자전거2", false, 20L),
                        Tuple.tuple(category, false, "자전거3", false, 30L)
                );
    }
}