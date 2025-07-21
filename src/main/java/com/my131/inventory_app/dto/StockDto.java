package com.my131.inventory_app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.my131.inventory_app.model.Product;
import com.my131.inventory_app.model.Warehouse;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class StockDto {

    @NotNull(message = "제품 ID를 선택해주세요.")
    private Long productId;

    @NotNull(message = "창고 ID를 선택해주세요.")
    private Long warehouseId;

    @NotNull(message = "수량을 입력해주세요.")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private Integer quantity;
}
