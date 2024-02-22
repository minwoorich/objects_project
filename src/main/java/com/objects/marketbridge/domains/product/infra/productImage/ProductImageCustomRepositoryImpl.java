package com.objects.marketbridge.domains.product.infra.productImage;

import com.objects.marketbridge.domains.product.dto.ProductImageDto;
import com.objects.marketbridge.domains.image.domain.QImage;
import com.objects.marketbridge.domains.product.domain.QProductImage;
import com.objects.marketbridge.domains.product.service.port.ProductImageCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductImageCustomRepositoryImpl implements ProductImageCustomRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<ProductImageDto> findAllByProductIdWithImage(Long productId) {
        QProductImage pi = new QProductImage("pi");
        QImage i = new QImage("i");

//        List<ProductImageDto> result = queryFactory
//                .select()
        return null;
    }
}
