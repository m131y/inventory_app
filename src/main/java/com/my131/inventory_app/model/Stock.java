package com.my131.inventory_app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    // 기본키
    // 생성전략 : 자동생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다대일관계
    // fetch = FetchType.LAZY : 지연 로딩 전략 설정, recipe는 실제로 사용할 때까지 DB에서 조회하지 않음
    @ManyToOne(fetch = FetchType.LAZY)
    // product_id 라는 외래 키(FK) 컬럼을 생성, DB단에서 null 방지 (물리적 제약)
    @JoinColumn(name = "product_id", nullable = false)
    // 자바 직렬화 순환 방지 대책, 이 필드는 직렬화 제외.
    @JsonBackReference
    private Product product;

    // 다대일관계
    // fetch = FetchType.LAZY : 지연 로딩 전략 설정, recipe는 실제로 사용할 때까지 DB에서 조회하지 않음
    @ManyToOne(fetch = FetchType.LAZY)
    // warehouse_id 라는 외래 키(FK) 컬럼을 생성, DB단에서 null 방지 (물리적 제약)
    @JoinColumn(name = "warehouse_id", nullable = false)
    // 자바 직렬화 순환 방지 대책, 이 필드는 직렬화 제외.
    @JsonBackReference
    private Warehouse warehouse;

    private Integer quantity;
}