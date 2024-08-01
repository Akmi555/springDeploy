package org.blb.DTO.rent.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    @Schema(description = "Category of the product", example = "{\"name\": \"Vehicles\"}")
    private String name;
}
