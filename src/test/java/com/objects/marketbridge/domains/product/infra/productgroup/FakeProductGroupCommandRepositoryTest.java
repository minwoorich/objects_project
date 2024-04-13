package com.objects.marketbridge.domains.product.infra.productgroup;

import com.objects.marketbridge.domains.product.domain.ProductGroup;
import com.objects.marketbridge.domains.product.mock.BaseFakeProductGroupRepository;
import com.objects.marketbridge.domains.product.mock.FakeProductGroupCommandRepository;
import com.objects.marketbridge.domains.product.mock.FakeProductGroupQueryRepository;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupCommandRepository;
import com.objects.marketbridge.domains.product.service.port.productgroup.ProductGroupQueryRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FakeProductGroupCommandRepositoryTest {
    ProductGroupCommandRepository productGroupCommandRepository = new FakeProductGroupCommandRepository();
    ProductGroupQueryRepository productGroupQueryRepository = new FakeProductGroupQueryRepository();

    @AfterEach
    void clear() {
        BaseFakeProductGroupRepository.getInstance().clear();
    }

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

    @DisplayName("DB에 저장된 ProductGroup 이 없을 경우 에러를 던진다")
    @Test
    void save_error() {
        // given
        Long inputId = 1L;

        // when
        Throwable thrown = catchThrowable(() -> productGroupQueryRepository.findById(inputId));

        //then
        assertThat(thrown).isInstanceOf(JpaObjectRetrievalFailureException.class)
                .hasMessageContaining("엔티티가 존재하지 않습니다");
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

    @DisplayName("ProductGroup 테이블에 아무것도 없을 경우 빈 배열을 반환한다")
    @Test
    void saveAll_empty() {
        // given

        // when
        List<ProductGroup> savedProductGroups = productGroupQueryRepository.findAllById(List.of(1L, 2L, 3L));

        //then
        assertThat(savedProductGroups).isNotNull();
        assertThat(savedProductGroups).hasSize(0);
        assertThat(savedProductGroups).isInstanceOf(List.class);
    }
}
