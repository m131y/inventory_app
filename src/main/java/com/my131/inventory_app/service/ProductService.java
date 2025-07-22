package com.my131.inventory_app.service;

import com.my131.inventory_app.dto.ProductDto;
import com.my131.inventory_app.model.Product;
import com.my131.inventory_app.query.ProductQueryRepository;
import com.my131.inventory_app.query.ProductSearchCondition;
import com.my131.inventory_app.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;

    // 상품 검색 조회
    // QueryDSL 기반 동적 조건 검색
    public List<Product> search(ProductSearchCondition condition) {
        // condition 으로 검색한 product 목록을 반환
        return productQueryRepository.search(condition);
    }

    // 단일 상품 조회
    public Product getById(Long id) {
       return productRepository.findById(id)
               .orElseThrow(()-> new NoSuchElementException("제품을 찾을 수 없습니다. id =" + id));
    }

    // 상품 등록
    public Product create(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return productRepository.save(product);
    }

    // 상품 수정
    public Product update(Long id, ProductDto productDto) {
        Product product = getById(id);
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        Product saved = productRepository.save(product);

        return productRepository.save(product);
    }
    // 상품 삭제
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
