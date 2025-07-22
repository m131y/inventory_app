package com.my131.inventory_app.Controller;

import com.my131.inventory_app.dto.WarehouseDto;
import com.my131.inventory_app.model.Warehouse;
import com.my131.inventory_app.query.WarehouseSearchCondition;
import com.my131.inventory_app.repository.WarehouseRepository;
import com.my131.inventory_app.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    // 창고 검색 조회
    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) {
        return warehouseService.getById(id);
    }

    // 창고 단일 조회
    @GetMapping
    public List<Warehouse> search(WarehouseSearchCondition condition) {
        return warehouseService.search(condition);
    }

    // 창고 등록
    @PostMapping
    public Warehouse create(@Valid @RequestBody WarehouseDto warehouseDto) {
        return warehouseService.create(warehouseDto);
    }

    // 창고 수정
    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Long id, @Valid @RequestBody WarehouseDto warehouseDto) {
        return warehouseService.update(id, warehouseDto);
    }

    // 창고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
