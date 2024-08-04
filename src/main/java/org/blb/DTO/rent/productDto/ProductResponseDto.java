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

    @Schema(description = "Id of current product", example = "1")
    private Long id;

    @Schema(description = "URL of the product image", example = "https://your-bucket-url/image.png")
    private String imageUrl;

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


    @Schema(description = "Owner of the product", example = "{\"name\": \"john\"}")
    private UserJustWithNameDto owner;

}
