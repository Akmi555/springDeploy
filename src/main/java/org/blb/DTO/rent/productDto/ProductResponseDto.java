package org.blb.DTO.rent.productDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.DTO.region.RegionDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.user.UserJustWithNameDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    @Schema(description = "Name of the product", example = "SUP")
    private String name;

    @Schema(description = "Category of the product", example = "{\"name\": \"Vehicles\"}")
    private CategoryCreateRequestDto category;

    @Schema(description = "Price of the product", example = "30.00")
    private Double price;

    @Schema(description = "Description of the product", example = "A high quality SUP suitable for a good time.")
    private String description;

    @Schema(description = "Region where the product is available", example = "{\"regionName\": \"Bremen\"}")
    private RegionJustWithNameDto region;

    //@Schema(description = "Indicates whether the product is in stock", example = "true")
    //private Boolean isInStock;

    @Schema(description = "Owner of the product", example = "{\"name\": \"john\"}")
    private UserJustWithNameDto owner;

}
