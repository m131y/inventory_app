package com.my131.inventory_app.service;

import com.my131.inventory_app.dto.WarehouseDto;
import com.my131.inventory_app.model.Warehouse;
import com.my131.inventory_app.query.WarehouseQueryRepository;
import com.my131.inventory_app.query.WarehouseSearchCondition;
import com.my131.inventory_app.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseQueryRepository queryRepository;

    // 단일 창고 조회
    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("제품을 찾을 수 없습니다. id =" + id));
    }

    // 창고 검색 조회
    // QueryDSL 기반 동적 조건 검색
    public List<Warehouse> search(WarehouseSearchCondition condition) {
        // condition 으로 검색한 warehouse 목록을 반환
         return queryRepository.search(condition);
    }

    // 창고 등록
    public Warehouse create(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDto.getName());
        warehouse.setLocation(warehouseDto.getLocation());

        return warehouseRepository.save(warehouse);
    }

    // 창고 수정
    public Warehouse update(Long id, WarehouseDto warehouseDto) {
        Warehouse warehouse = getById(id);
        if (warehouseDto.getName() != null && !warehouseDto.getName().isBlank()) {
            warehouse.setName(warehouseDto.getName());
        }
        if (warehouseDto.getLocation() != null && !warehouseDto.getLocation().isBlank()) {
            warehouse.setLocation(warehouseDto.getLocation());
        }

        return warehouseRepository.save(warehouse);
    }

    // 창고 삭제
    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }
}
