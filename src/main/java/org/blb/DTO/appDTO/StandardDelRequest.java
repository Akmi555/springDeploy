package org.blb.DTO.appDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "standard request to del with object id")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardDelRequest {
    @Schema(description = "id of object", example = "7")
    @Min(1)
    @NotNull
    private Long id;
}
