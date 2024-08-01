package org.blb.DTO.appDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Message", description = "Any message from server")
public class StandardResponseDto {
    @Schema(description = "Possible: error message, status change, etc.", example = "User not found")
    private String message;
}
