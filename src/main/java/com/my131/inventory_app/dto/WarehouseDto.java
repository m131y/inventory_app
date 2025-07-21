package com.my131.inventory_app.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto {

    @NotBlank(message = "창고명을 입력해주세요")
    private String name;

    @Size(max = 200, message = "위치 정보는 최대 200자까지 허용됩니다")
    private String location;

}
