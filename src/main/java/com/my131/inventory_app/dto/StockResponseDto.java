package com.my131.inventory_app.dto;

import com.my131.inventory_app.model.Stock;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Integer quantity;

    public static StockResponseDto fromEntity(Stock stock) {
        return new StockResponseDto(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getWarehouse().getId(),
                stock.getWarehouse().getName(),
                stock.getQuantity()
        );
    }
}
