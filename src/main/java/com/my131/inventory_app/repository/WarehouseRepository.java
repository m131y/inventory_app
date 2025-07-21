package com.my131.inventory_app.repository;

import com.my131.inventory_app.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, QuerydslPredicateExecutor {

}
