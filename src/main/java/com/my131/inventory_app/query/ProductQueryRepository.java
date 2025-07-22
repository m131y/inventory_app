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
        // QueryDSL이 자동 생성한 Q타입 클래스 (각각의 Entity 기반)
        // 각 테이블을 별칭(alias)처럼 표현하여 쿼리 작성에 사용
        QProduct product = QProduct.product;

        // 여러 조건을 and()로 누적할 수 있는 QueryDSL 전용 조건 조립 도구
        // 조건을 if 문으로 유동적으로 누적 → Null-safe한 동적 쿼리 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 상품명: like '%keyword%' (대소문자 구분 없이)
        if (condition.getName() != null && !condition.getName().isBlank()) {
            builder.and(product.name.containsIgnoreCase(condition.getName()));
        }
        // 수량 조건: >= min, <= max
        if (condition.getMinPrice() != null) {
            builder.and(product.price.goe(condition.getMinPrice()));
        }
        if (condition.getMaxPrice() != null) {
            builder.and(product.price.loe(condition.getMaxPrice()));
        }
        // 최종 쿼리 실행
        // selectFrom(product): product 기준 조회
        // where(builder): 위에서 누적된 조건 적용
        // orderBy(...): 결과 정렬 (상품명 오름차순)
        // fetch(): 최종 결과 리스트 반환
        return queryFactory
                .selectFrom(product)
                .where(builder)
                .orderBy(product.name.asc())
                .fetch();
    }
}