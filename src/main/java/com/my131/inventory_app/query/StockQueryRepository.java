package com.my131.inventory_app.query;

import com.my131.inventory_app.model.QProduct;
import com.my131.inventory_app.model.QStock;
import com.my131.inventory_app.model.QWarehouse;
import com.my131.inventory_app.model.Stock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Stock> search(StockSearchCondition condition){
        QStock stock = QStock.stock;
        QProduct product = QProduct.product;
        QWarehouse warehouse = QWarehouse.warehouse;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(stock.product.id.isNotNull());
        builder.and(stock.warehouse.id.isNotNull());

        if (condition.getProductName() != null && !condition.getProductName().isBlank()) {
            builder.and(product.name.containsIgnoreCase(condition.getProductName()));
        }
        if (condition.getWarehouseName() != null && !condition.getWarehouseName().isBlank()) {
            builder.and(warehouse.name.containsIgnoreCase(condition.getWarehouseName()));
        }
        if (condition.getMinQuantity() != null) {
            builder.and(stock.quantity.goe(condition.getMinQuantity()));
        }
        if (condition.getMaxQuantity() != null) {
            builder.and(stock.quantity.loe(condition.getMaxQuantity()));
        }

        return jpaQueryFactory
                .selectFrom(stock)
                .join(stock.product, product)
                .join(stock.warehouse, warehouse)
                .where(builder)
                .orderBy(product.name.asc(), warehouse.name.asc())
                .fetch();
        }
}
