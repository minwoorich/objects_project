package com.objects.marketbridge.domains.product.domain;

import com.objects.marketbridge.domains.category.domain.Category;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class ProductGroupTest {

    @DisplayName("ProductGroup 을 생성 할 수 있다")
    @Test
    void create() {
        // given
        Category category = Category.builder().name("의류").build();
        String name = "긴팔 티셔츠";
        Boolean isOwn = false;
        Boolean isSubs = false;
        Long discountRate = 12L;

        // when
        ProductGroup productGroup = ProductGroup.create(category, isOwn, name, isSubs, discountRate);

        //then
        assertThat(productGroup).extracting("category", "name", "isOwn", "isSubs", "discountRate")
                .containsExactly(category, "긴팔 티셔츠", false, false, 12L);

    }

}