package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "data use for change user state")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStateDTO {
    @NotNull
    @Min(1)
    @Schema(description = "User id", example = "7")
    private  Long id;
    @NotNull
    @Schema(description = "false - user has state 'BANNED'", example = "false")
    private Boolean confirmed;
}
