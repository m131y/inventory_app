package com.my131.inventory_app.Controller;

import com.my131.inventory_app.dto.StockDto;
import com.my131.inventory_app.dto.StockUpdateDto;
import com.my131.inventory_app.model.Stock;
import com.my131.inventory_app.query.StockSearchCondition;
import com.my131.inventory_app.service.StockService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/{id}")
    public Stock getById(@PathVariable Long id) {
        return stockService.getById(id);
    }

    // condition은 빈 객체아닌가 ? 어떻게 여기서 getproductname 같은게 실행되는거지 ㅜ
    @GetMapping
    public List<Stock> search(StockSearchCondition condition){
        return stockService.search(condition);
    }

    @PostMapping
    public Stock create(@Valid @RequestBody StockDto stockDto) {
        return stockService.create(stockDto);
    }

    @PutMapping("/{id}")
    public Stock update(@PathVariable Long id, @Valid @RequestBody StockUpdateDto stockUpdateDto) {
        return stockService.update(id, stockUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stockService.delete(id);
        return ResponseEntity.noContent().build();
    }

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
