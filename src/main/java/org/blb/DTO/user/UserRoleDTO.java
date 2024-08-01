package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "data use for change user role")
public class UserRoleDTO {

    @NotNull
    @Min(1)
    @Schema(description = "User id", example = "7")
    private Long userId;
    @Schema(description = "false - user has state 'USER' - true 'ADMIN'", example = "false")
    @NotNull
    private Long roleId;
}
