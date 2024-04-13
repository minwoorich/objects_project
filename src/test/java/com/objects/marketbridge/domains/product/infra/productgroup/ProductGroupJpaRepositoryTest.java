package com.objects.marketbridge.domains.product.infra.productgroup;

import com.objects.marketbridge.domains.product.domain.ProductGroup;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupCommandRepository;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupQueryRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@ActiveProfiles("test")
class ProductGroupJpaRepositoryTest {

    @Autowired ProductGroupCommandRepository productGroupCommandRepository;
    @Autowired ProductGroupQueryRepository productGroupQueryRepository;

    @DisplayName("ProductGroup 을 저장 할 수 있다")
    @Test
    void save() {
        // given
        ProductGroup productGroup = ProductGroup.builder().name("긴팔 티셔츠").isOwn(false).build();

        // when
        productGroupCommandRepository.save(productGroup);
        ProductGroup savedProductGroup = productGroupQueryRepository.findById(productGroup.getId());

        //then
        assertThat(savedProductGroup)
                .extracting("id", "name", "isOwn")
                .containsExactly(1L, "긴팔 티셔츠", false);
    }



    @DisplayName("ProductGroup 리스트를 한꺼번에 저장 할 수 있다")
    @Test
    void saveAll() {
        // given
        ProductGroup productGroup1 = ProductGroup.builder().name("긴팔 티셔츠1").isOwn(false).build();
        ProductGroup productGroup2 = ProductGroup.builder().name("긴팔 티셔츠2").isOwn(false).build();
        ProductGroup productGroup3 = ProductGroup.builder().name("긴팔 티셔츠3").isOwn(true).build();

        // when
        productGroupCommandRepository.saveAll(List.of(productGroup1, productGroup2, productGroup3));
        List<ProductGroup> savedProductGroups = productGroupQueryRepository.findAllById(List.of(productGroup1.getId(), productGroup2.getId(), productGroup3.getId()));

        //then
        assertThat(savedProductGroups)
                .extracting(ProductGroup::getId, ProductGroup::getName, ProductGroup::getIsOwn)
                .containsExactly(
                        Tuple.tuple(1L, "긴팔 티셔츠1", false),
                        Tuple.tuple(2L, "긴팔 티셔츠2", false),
                        Tuple.tuple(3L, "긴팔 티셔츠3", true));
    }
}