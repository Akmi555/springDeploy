package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(description = "User list for admin panel")
public class UserResponseDTO {
    @Schema(description = "User`s id", example = "7")
    private  Long id;
    @Schema(description = "User`s email", example = "user@email.com")
    private String email;
    @Schema(description = "User`s name", example = "Tom")
    private String name;
    @Schema(description = "User`s role", example = "ADMIN")
    private String role;
    @Schema(description = "User`s state", example = "CONFIRMED")
    private String state;

}
