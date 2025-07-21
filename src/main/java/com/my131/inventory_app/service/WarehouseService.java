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

    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("제품을 찾을 수 없습니다. id =" + id));
    }

    // condition == dto
    public List<Warehouse> search(WarehouseSearchCondition condition) {
        return queryRepository.search(condition);
    }

    public Warehouse create(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDto.getName());
        warehouse.setLocation(warehouseDto.getLocation());

        return warehouseRepository.save(warehouse);
    }

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

    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }
}
