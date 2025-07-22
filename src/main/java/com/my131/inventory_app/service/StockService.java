package com.my131.inventory_app.service;

import com.my131.inventory_app.dto.StockDto;
import com.my131.inventory_app.dto.StockResponseDto;
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

    // 단일 재고 조회
    public StockResponseDto getById(Long id) {;
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("재고를 찾을 수 없습니다."));
        //.fromEntity = Stock -> StockResponseDto로 만드는 메서드
        return StockResponseDto.fromEntity(stock);
    }

    // 재고 검색 조회
    // QueryDSL 기반 동적 조건 검색
    public List<StockResponseDto> search(StockSearchCondition condition) {
        // condition 으로 검색한 stock 목록을 반환
        List<Stock> stocks = queryRepository.search(condition);
        // Stock List를 StockResponseDto List로 매핑
        return  stocks.stream()
                .map((stock) -> StockResponseDto.fromEntity(stock)).toList();
    }

    // 재고 등록
    public StockResponseDto create(StockDto stockDto) {
        // getByID 안쓰는 이유 = 의존관계가 늘어남
        Product product = productRepository.findById(stockDto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("제품을 찾을 수 없습니다."));
        Warehouse warehouse = warehouseRepository.findById(stockDto.getWarehouseId())
                .orElseThrow(() -> new NoSuchElementException("창고를 찾을 수 없습니다."));
        // 새로운 Stock 생성해 필드 설정
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setWarehouse(warehouse);
        stock.setQuantity(stockDto.getQuantity());
        // Stock 저장하고 반환된 Stock을 .fromEntity()사용해 StockResponseDto로 변환
        return StockResponseDto.fromEntity(stockRepository.save(stock));
    }

    // 재고 수량 수정
    public StockResponseDto update(Long id, StockUpdateDto stockUpdateDto){
        // id에 맞는 stock 있으면 반환, 없으면 오류
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("재고를 찾을 수 없습니다."));
        // 업데이트를 위해 받은 quantity 정보를 반환받은 stock에 덮어씌움
        stock.setQuantity(stockUpdateDto.getQuantity());
        // 업데이트 된 stock 저장 후 반환받은 stock responseDto로 변환
        return StockResponseDto.fromEntity(stockRepository.save(stock));
    }

    // 재고 삭제
    public void delete(Long id) {
        stockRepository.deleteById(id);
    }

    // 재고 이동
    public void transferStock(Long productId, Long srcWarehouseId, Long destWarehouseId, int quantity) {
        // id에 맞는 product 있으면 반환, 없으면 오류
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("제품을 찾을 수 없습니다."));
        // id에 맞는 warehouse 있으면 반환, 없으면 오류
        Warehouse srcWarehouse = warehouseRepository.findById(srcWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("출발 창고를 찾을 수 없습니다."));
        // id에 맞는 warehouse 있으면 반환, 없으면 오류
        Warehouse destWarehouse = warehouseRepository.findById(destWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("도착 창고를 찾을 수 없습니다."));
        // 출발 창고에 상품 id 맞는 stock 있으면 반환, 없으면 오류
        Stock srcStock = stockRepository.findByProductIdAndWarehouseId(productId, srcWarehouseId)
                .orElseThrow(() -> new NoSuchElementException("출발 재고가 존재하지 않습니다."));

        // 출발 창고의 재고 수량이 이동 수량보다 적으면 출발 재고 부족 오류
        if(srcStock.getQuantity() < quantity) {
            throw new IllegalStateException("출발 재고가 부족합니다.");
        }
        // 출발 창고의 재고 수량이 이동 수량보다 많으면 이동 ok
        // 출발 창고의 재고 수량 (현 수량 - 이동 재고 수량) 으로 업데이트
        srcStock.setQuantity(srcStock.getQuantity() - quantity);
        // 도착 창고에 상품 id 맞는 stock 있으면 반환, 없으면 재고 객체 새로 생성
        Stock destStock = stockRepository.findByProductIdAndWarehouseId(productId,destWarehouseId)
                .orElse(new Stock());
        // 도착 재고에 상품, 창고, 수량 설정
        destStock.setProduct(product);
        destStock.setWarehouse(destWarehouse);
        // 전에 재고가 없던 상품이라 새로 생성된 객체라면(수량이 null이라면) 수량을 이동 재고로 설정
        // 전에 재고가 있던 상품이면 현 재고 수량 + 이동 재고로 설정
        destStock.setQuantity(destStock.getQuantity() == null ? quantity : destStock.getQuantity() + quantity);
        // 변경된 출발 재고 + 도착 재고를 한 번에 저장
        stockRepository.saveAll(List.of(srcStock, destStock));
    }
}
