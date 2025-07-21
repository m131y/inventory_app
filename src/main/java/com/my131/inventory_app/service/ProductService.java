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

    public List<Product> search(ProductSearchCondition condition) {
        return productQueryRepository.search(condition);
    }

    public Product getById(Long id) {
       return productRepository.findById(id)
               .orElseThrow(()-> new NoSuchElementException("제품을 찾을 수 없습니다. id =" + id));
    }

    public Product create(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return productRepository.save(product);
    }

    public Product update(Long id, ProductDto productDto) {
        Product product = getById(id);
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        Product saved = productRepository.save(product);

        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
