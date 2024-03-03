package com.objects.marketbridge.common.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;

public class MyQueryDslUtil {
    public static OrderSpecifier<?> getSortedColumn(Sort.Order order, Path<?> parent, String fieldName) {

        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(direction, fieldPath);
    }
}
