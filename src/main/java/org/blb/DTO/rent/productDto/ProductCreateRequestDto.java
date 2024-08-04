package org.blb.DTO.rent.productDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.DTO.region.RegionDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDto {

    @Schema(description = "Product image in base64 format")
    private String imageUrl;

    @NotEmpty(message = "Product name cannot be empty")
    @NotBlank(message = "Product name must be not blank.")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Product name can contain only latin characters, digits, and spaces.")
    @Schema(description = "Name of product", example = "SUP")
    private String name;

    @NotNull(message = "Product category must be not null.")
    @Schema(description = "Category of product", example = "{\"name\": \"Vehicles\"}")
    private CategoryCreateRequestDto category;

    @NotNull(message = "Product price must be not null.")
    @Schema(description = "Price of product", example = "30")
    private Double price;

    @Schema(description = "Product description", example = "A high quality SUP suitable for a good time.")
    private String description;
    @Schema(description = "Indicates whether the product is in stock", example = "true")
    private Boolean isInStock;

    @NotNull(message = "Region by product must be not null.")
    @Schema(description = "Region where the product is available", example = "Bremen")
    private RegionJustWithNameDto region;

}
