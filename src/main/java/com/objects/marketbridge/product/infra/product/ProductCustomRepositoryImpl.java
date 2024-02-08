//package com.objects.marketbridge.product.infra.product;
//
//import com.objects.marketbridge.product.domain.Product;
//import com.objects.marketbridge.product.dto.ProductSearchConditionDto;
//import com.objects.marketbridge.product.dto.ProductSimpleDto;
//import com.objects.marketbridge.product.service.port.ProductCustomRepository;
//import com.querydsl.core.BooleanBuilder;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import java.util.List;
//import java.util.Objects;
//
//import static com.objects.marketbridge.category.domain.QCategory.category;
//import static com.objects.marketbridge.product.domain.QProduct.product;
//
//public class ProductCustomRepositoryImpl extends QuerydslRepositorySupport implements ProductCustomRepository {
//    private final JPAQueryFactory queryFactory;
//
//    public ProductCustomRepositoryImpl(JPAQueryFactory queryFactory){
//        super(Product.class);
//        this.queryFactory = queryFactory;
//    }
//
//    @Override
//    public Page<ProductSimpleDto> findAllProductWithCondition(ProductSearchConditionDto conditionDto, Pageable pageable) {
//        JPAQuery<ProductSimpleDto> query = queryFactory
//                .select(Projections.constructor(ProductSimpleDto.class,
//                        product.id,product.thumbImg,product.name,product.isOwn,product.price,product.discountRate))
//                .from(product,category)
//                .where(productSearchPredicate(conditionDto));
//
//        //count 분리
//        Long count = queryFactory.select(product.count())
//                .from(product).where(productSearchPredicate(conditionDto)).fetchFirst();
//        List<ProductSimpleDto> result = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable,query).fetch();
//        return new PageImpl<>(result,pageable,count);
//    }
//
//    private BooleanBuilder productSearchPredicate(ProductSearchConditionDto conditionDto){
//        return new BooleanBuilder()
//                .and(productLevelOrCondition(conditionDto.getLeafDepthInfoList())); // 옵션1
//    }
//
//    private BooleanBuilder productLevelOrCondition(List<Long> leafCategories){
//        BooleanBuilder builder = new BooleanBuilder();
//        leafCategories.stream().forEach(l -> builder.or(product.));
//    }
//}
