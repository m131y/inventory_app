package com.my131.inventory_app.repository;

import com.my131.inventory_app.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, QuerydslPredicateExecutor {
    // Optional<Stock> : 검색 결과 없을 시 Optional.empty()로 안전하게 처리
    // findBy : JPA가 이해하는 키워드 → 조회용 쿼리임을 나타냄,
    // ProductIdAndWarehouseId : Stock 엔티티에 있는 productId, WarehouseID를 기준으로 검색
    Optional<Stock> findByProductIdAndWarehouseId(Long productId, Long WarehouseId);

}
