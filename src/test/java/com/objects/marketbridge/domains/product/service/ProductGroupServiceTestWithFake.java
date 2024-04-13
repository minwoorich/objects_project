package com.objects.marketbridge.domains.product.service;

import com.objects.marketbridge.domains.category.domain.Category;
import com.objects.marketbridge.domains.order.mock.FakeProductRepository;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.domain.ProductGroup;
import com.objects.marketbridge.domains.product.domain.ProductToProductGroupService;
import com.objects.marketbridge.domains.product.mock.BaseFakeProductGroupRepository;
import com.objects.marketbridge.domains.product.mock.FakeProductGroupCommandRepository;
import com.objects.marketbridge.domains.product.mock.FakeProductGroupQueryRepository;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupCommandRepository;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupQueryRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductGroupServiceTestWithFake {

    ProductRepository productRepository = new FakeProductRepository();
    ProductGroupCommandRepository productGroupCommandRepository = new FakeProductGroupCommandRepository();
    ProductGroupQueryRepository productGroupQueryRepository = new FakeProductGroupQueryRepository();
    ProductToProductGroupService productToProductGroupService = new ProductToProductGroupService();

    ProductGroupService productGroupService = new ProductGroupService(
            productToProductGroupService,
            productRepository,
            productGroupCommandRepository
    );

    @AfterEach
    void clear() {
        productRepository.deleteAllInBatch();
        BaseFakeProductGroupRepository.getInstance().clear();
    }

    @DisplayName("product 의 데이터를 product_group 으로 옮길 수 있다")
    @Test
    void productToProductGroup() {

        // given
        Category category = Category.builder().name("의류").level(1L).build();
        Product product1 = Product.builder().name("자전거1").price(1000L).isOwn(false).stock(100L).discountRate(10L).isSubs(false).build();
        product1.setCategory(category);

        Product product2 = Product.builder().name("자전거2").price(2000L).isOwn(false).stock(200L).discountRate(20L).isSubs(false).build();
        product2.setCategory(category);

        Product product3 = Product.builder().name("자전거3").price(3000L).isOwn(false).stock(300L).discountRate(30L).isSubs(false).build();
        product3.setCategory(category);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        productGroupService.productToProductGroup();
        List<ProductGroup> productGroups = productGroupQueryRepository.findAll();

        //then
        assertThat(productGroups).hasSize(3);
        assertThat(productGroups).hasSize(3);
        assertThat(productGroups)
                .extracting(ProductGroup::getId, ProductGroup::getCategory, ProductGroup::getIsOwn, ProductGroup::getName, ProductGroup::getIsSubs, ProductGroup::getDiscountRate)
                .containsExactly(
                        Tuple.tuple(1L, category, false, "자전거1", false, 10L),
                        Tuple.tuple(2L, category, false, "자전거2", false, 20L),
                        Tuple.tuple(3L, category, false, "자전거3", false, 30L)
                );
    }

}