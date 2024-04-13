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


class FakeProductGroupQueryRepositoryTest {

    ProductGroupCommandRepository productGroupCommandRepository = new FakeProductGroupCommandRepository();
    ProductGroupQueryRepository productGroupQueryRepository = new FakeProductGroupQueryRepository();

    @AfterEach
    void clear() {
        BaseFakeProductGroupRepository.getInstance().clear();
    }

    @DisplayName("id로 ProductGroup 을 찾을 수 있다")
    @Test
    void findById() {
        // given
        ProductGroup productGroup1 = ProductGroup.builder().name("긴팔 티셔츠").build();
        ProductGroup productGroup2 = ProductGroup.builder().name("자전거").build();
        ProductGroup productGroup3 = ProductGroup.builder().name("마우스").build();

        productGroupCommandRepository.saveAll(List.of(productGroup1, productGroup2, productGroup3));

        // when
        ProductGroup savedProductGroup = productGroupQueryRepository.findById(1L);

        //then
        assertThat(savedProductGroup).extracting("id", "name").containsExactly(1L, "긴팔 티셔츠");
    }

    @DisplayName("찾는 엔티티가 없을 경우 에러를 발생시킨다")
    @Test
    void findById_error() {
        // given
        ProductGroup productGroup1 = ProductGroup.builder().name("긴팔 티셔츠").build();
        ProductGroup productGroup2 = ProductGroup.builder().name("자전거").build();
        ProductGroup productGroup3 = ProductGroup.builder().name("마우스").build();

        productGroupCommandRepository.saveAll(List.of(productGroup1, productGroup2, productGroup3));

        // when
        Throwable thrown = catchThrowable(() -> productGroupQueryRepository.findById(4L));

        //then
        assertThat(thrown).isInstanceOf(JpaObjectRetrievalFailureException.class)
                .hasMessageContaining("엔티티가 존재하지 않습니다");
    }

    @DisplayName("id를 통해 일치하는 모든 엔티티를 반환한다")
    @Test
    void findAllById() {
        // given
        ProductGroup productGroup1 = ProductGroup.builder().name("긴팔 티셔츠").build();
        ProductGroup productGroup2 = ProductGroup.builder().name("자전거").build();
        ProductGroup productGroup3 = ProductGroup.builder().name("마우스").build();

        productGroupCommandRepository.saveAll(List.of(productGroup1, productGroup2, productGroup3));

        // when
        List<ProductGroup> productGroups = productGroupQueryRepository.findAllById(List.of(1L, 2L, 3L));

        //then
        assertThat(productGroups).hasSize(3);
        assertThat(productGroups)
                .extracting(ProductGroup::getId, ProductGroup::getName)
                .containsExactly(
                        Tuple.tuple(1L, "긴팔 티셔츠"),
                        Tuple.tuple(2L, "자전거"),
                        Tuple.tuple(3L, "마우스")
                );

    }

    @DisplayName("ProductGroup 테이블에 아무것도 없을 경우 빈 배열을 반환한다")
    @Test
    void findAllById_empty() {
        // given

        // when
        List<ProductGroup> savedProductGroups = productGroupQueryRepository.findAllById(List.of(1L, 2L, 3L));

        //then
        assertThat(savedProductGroups).isNotNull();
        assertThat(savedProductGroups).hasSize(0);
        assertThat(savedProductGroups).isInstanceOf(List.class);
    }

    @DisplayName("ProductGroup 테이블에 저장된 모든 엔티티를 반환한다")
    @Test
    void findAll() {
        // given
        ProductGroup productGroup1 = ProductGroup.builder().name("긴팔 티셔츠").build();
        ProductGroup productGroup2 = ProductGroup.builder().name("자전거").build();
        ProductGroup productGroup3 = ProductGroup.builder().name("마우스").build();

        productGroupCommandRepository.saveAll(List.of(productGroup1, productGroup2, productGroup3));

        // when
        List<ProductGroup> productGroups = productGroupQueryRepository.findAllById(List.of(1L, 2L, 3L));

        //then
        assertThat(productGroups).hasSize(3);
        assertThat(productGroups)
                .extracting(ProductGroup::getId, ProductGroup::getName)
                .containsExactly(
                        Tuple.tuple(1L, "긴팔 티셔츠"),
                        Tuple.tuple(2L, "자전거"),
                        Tuple.tuple(3L, "마우스")
                );
    }

    @DisplayName("ProductGroup 테이블에 아무것도 없을 경우 빈 배열을 반환한다")
    @Test
    void findAll_empty() {
        // given

        // when
        List<ProductGroup> savedProductGroups = productGroupQueryRepository.findAllById(List.of(1L, 2L, 3L));

        //then
        assertThat(savedProductGroups).isNotNull();
        assertThat(savedProductGroups).hasSize(0);
        assertThat(savedProductGroups).isInstanceOf(List.class);
    }

}