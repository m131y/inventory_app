package com.my131.inventory_app.query;

import com.my131.inventory_app.model.Product;
import com.my131.inventory_app.model.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Product> search(ProductSearchCondition condition) {
        QProduct product = QProduct.product;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (condition.getName() != null && !condition.getName().isBlank()) {
            booleanBuilder.and(product.name.containsIgnoreCase(condition.getName()));
        }
        if (condition.getMinPrice() != null) {
            booleanBuilder.and(product.price.goe(condition.getMinPrice()));
        }
        if (condition.getMaxPrice() != null) {
            booleanBuilder.and(product.price.loe(condition.getMaxPrice()));
        }

        return queryFactory.selectFrom(product).where(booleanBuilder).orderBy(product.name.asc()).fetch();
    }
}