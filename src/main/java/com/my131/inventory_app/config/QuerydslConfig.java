package com.my131.inventory_app.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring에게 이 클래스가 설정 클래스임을 알림
// 내부에 정의된 @Bean 메서드들을 Spring Container에 등록
@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {
    // JPA의 핵심 객체인 EntityManager를 주입받음
    // JPAQueryFactory는 내부적으로 이 EntityManager를 통해 쿼리를 실행함
    private final EntityManager entityManager;

    // JPAQueryFactory를 Spring Bean으로 등록
    @Bean
    // JPAQueryFactory는 QueryDSL의 핵심 쿼리 작성 도구
    // QueryDSL로 동적 쿼리를 작성할 때 JPAQueryFactory 객체가 필요
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
