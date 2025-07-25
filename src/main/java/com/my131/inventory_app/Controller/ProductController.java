package com.my131.inventory_app.Controller;

import com.my131.inventory_app.dto.ProductDto;
import com.my131.inventory_app.model.Product;
import com.my131.inventory_app.query.ProductQueryRepository;
import com.my131.inventory_app.query.ProductSearchCondition;
import com.my131.inventory_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 상품 검색 조회
    @GetMapping
    public List<Product> search(ProductSearchCondition condition) {
        return productService.search(condition);
    }

    // 상품 단일 조회
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // 상품 등록
    @PostMapping
    public Product create(@Valid @RequestBody ProductDto dto) {
        return productService.create(dto);
    }

    // 상품 수정
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductDto dto) {
        return productService.update(id, dto);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}