package com.objects.marketbridge.product.infra.product;

import com.objects.marketbridge.category.domain.QCategory;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.domain.QProduct;
import com.objects.marketbridge.product.service.port.ProductCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Product findByIdwithCategory(Long id) {
        QCategory category = new QCategory("category");
        QProduct product = new QProduct("product");

        Product findProduct = queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.category, category).fetchJoin()
                .where(product.id.eq(id))
                .fetchOne();

        if (findProduct == null){
            return Product.builder().name("EMPTY").build();
        }

        return findProduct;
    }
}
