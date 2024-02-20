package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.domain.QCategory;
import com.objects.marketbridge.category.service.port.CategoryCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public String findByChildId(Long categoryId) {
        QCategory c1 = new QCategory("c1");
        QCategory c2 = new QCategory("c2");
        QCategory c3 = new QCategory("c3");

        List<String> result = new ArrayList<>();

        Tuple tuple = queryFactory
                .select(c3.name, c2.name, c1.name)
                .from(c1)
                .leftJoin(c2).on(
                        c2.id.eq(c1.parentId)
                                .and(c1.id.ne(c1.parentId)))
                .leftJoin(c3).on(
                        c3.id.eq(c2.parentId)
                                .and(c2.id.ne(c2.parentId)))
                .where(
                        c1.id.eq(categoryId)
                ).fetchOne();
        for (int i = 0; i < tuple.size(); i++) {
            if (tuple.get(i,String.class) != null){
                result.add(tuple.get(i,String.class));
            }
        }

        return String.join(">",result);
    }
}
