package com.my131.inventory_app.Controller;

import com.my131.inventory_app.dto.StockDto;
import com.my131.inventory_app.dto.StockResponseDto;
import com.my131.inventory_app.dto.StockUpdateDto;
import com.my131.inventory_app.model.Stock;
import com.my131.inventory_app.query.StockSearchCondition;
import com.my131.inventory_app.service.StockService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    // 재고 단일 조회
    @GetMapping("/{id}")
    public StockResponseDto getById(@PathVariable Long id) {
        return stockService.getById(id);
    }

    // 재고 검색 조회
    // StockSearchCondition 은 Springboot가 자동 매핑 = @ModelAttribute
    @GetMapping
    public List<StockResponseDto> search(StockSearchCondition condition){
        return stockService.search(condition);
    }

    // 재고 등록
    @PostMapping
    public StockResponseDto create(@Valid @RequestBody StockDto stockDto) {
        return stockService.create(stockDto);
    }

    // 재고 수정
    @PutMapping("/{id}")
    public StockResponseDto update(@PathVariable Long id, @Valid @RequestBody StockUpdateDto stockUpdateDto) {
        return stockService.update(id, stockUpdateDto);
    }

    // 재고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 재고 이동
    // 주소 뒤의 파라미터로 매개변수 받아옴
    @PostMapping("/transfer")
    public void transfer(
            @RequestParam Long productId,
            @RequestParam Long srcWarehouseId,
            @RequestParam Long destWarehouseId,
            @RequestParam int quantity
    ) {
        stockService.transferStock(productId, srcWarehouseId, destWarehouseId, quantity);
    }
}
