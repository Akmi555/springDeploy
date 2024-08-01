package org.blb.DTO.mainPageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="Main page billboard")
public class MpRentResponse {
    @Schema(description = "Id of billboard", example = "7")
    private Long id;
    @Schema(description = "Title of billboard", example = "Verkaufe einen Tish")
    private String name;
    @Schema(description = "Price", example = "50")
    private Double price;
    @Schema(description = "Region of billboard", example = "Berlin")
    private String region;

}
