package com.my131.inventory_app.service;

import com.my131.inventory_app.dto.StockDto;
import com.my131.inventory_app.dto.StockUpdateDto;
import com.my131.inventory_app.model.Product;
import com.my131.inventory_app.model.Stock;
import com.my131.inventory_app.model.Warehouse;
import com.my131.inventory_app.query.StockQueryRepository;
import com.my131.inventory_app.query.StockSearchCondition;
import com.my131.inventory_app.repository.ProductRepository;
import com.my131.inventory_app.repository.StockRepository;
import com.my131.inventory_app.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {
    private final StockRepository stockRepository;
    private final StockQueryRepository queryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public Stock getById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("재고를 찾을 수 없습니다."));
    }

    public List<Stock> search(StockSearchCondition condition) {
        return queryRepository.search(condition);
    }

    // getByID 안쓰는 이유 = 의존관계가 늘어남
    public Stock create(StockDto stockDto) {
        Product product = productRepository.findById(stockDto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("제품을 찾을 수 없습니다."));
        Warehouse warehouse = warehouseRepository.findById(stockDto.getWarehouseId())
                .orElseThrow(() -> new NoSuchElementException("창고를 찾을 수 없습니다."));

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(stockDto.getQuantity());

        return stockRepository.save(stock);
    }

    public Stock update(Long id, StockUpdateDto stockUpdateDto){
        Stock stock = getById(id);
//        Product product = productRepository.findById(stockDto.getProductId())
//                .orElseThrow(() -> new NoSuchElementException("제품을 찾을 수 없습니다."));
//        Warehouse warehouse = warehouseRepository.findById(stockDto.getWarehouseId())
//                .orElseThrow(() -> new NoSuchElementException("창고를 찾을 수 없습니다."));
//
//        stock.setProduct(product);
//        stock.setWarehouse(warehouse);
        stock.setQuantity(stockUpdateDto.getQuantity());

        return stockRepository.save(stock);
    }

    public void delete(Long id) {
        stockRepository.deleteById(id);
    }

    public void transferStock(Long productId, Long srcWarehouseId, Long destWarehouseId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("제품을 찾을 수 없습니다."));
        Warehouse srcWarehouse = warehouseRepository.findById(srcWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("출발 창고를 찾을 수 없습니다."));
        Warehouse destWarehouse = warehouseRepository.findById(destWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("도착 창고를 찾을 수 없습니다."));

        Stock srcStock = stockRepository.findByProductIdAndWarehouseId(productId, srcWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("출발 재고가 존재하지 않습니다."));

        if(srcStock.getQuantity() < quantity) {
            throw new IllegalStateException("출발 재고가 부족합니다.");
        }
        srcStock.setQuantity(srcStock.getQuantity() - quantity);

        Stock destStock = stockRepository.findByProductIdAndWarehouseId(productId,destWarehouseId)
                .orElse(new Stock());
        destStock.setProduct(product);
        destStock.setWarehouse(destWarehouse);
        destStock.setQuantity(destStock.getQuantity() == null ? quantity : destStock.getQuantity() + quantity);

        stockRepository.saveAll(List.of(srcStock, destStock));
    }
}
