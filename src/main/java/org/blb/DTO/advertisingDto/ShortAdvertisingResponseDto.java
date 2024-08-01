package org.blb.DTO.advertisingDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "advertising data")
public class ShortAdvertisingResponseDto {
    @Schema(name = "advertising id",example = "1")
    private Long advertisingId;
    @Schema(name = "advertising id",example = "some title")
    private String title;
//    @Schema(name = "advertising id",example = "some text description ")
//    private String description;
}
