package com.my131.inventory_app.query;

import com.my131.inventory_app.model.QWarehouse;
import com.my131.inventory_app.model.Warehouse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WarehouseQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Warehouse> search(WarehouseSearchCondition condition) {
        QWarehouse warehouse = QWarehouse.warehouse;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (condition.getName() != null && !condition.getName().isBlank()) {
            booleanBuilder.and(warehouse.name.containsIgnoreCase(condition.getName()));
        }
        if (condition.getLocation() != null && !condition.getLocation().isBlank()) {
            booleanBuilder.and(warehouse.location.containsIgnoreCase(condition.getLocation()));
        }

        return jpaQueryFactory.selectFrom(warehouse).where(booleanBuilder).orderBy(warehouse.name.asc()).fetch();
    }
}
