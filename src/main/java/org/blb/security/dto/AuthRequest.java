package org.blb.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Authentication request", description = "Data for authentication")
public class AuthRequest {
    @NotNull
    @Schema(description = "User`s email", example = "email@some.com")
    private String email;
    @NotNull
    @Schema(description = "User`s password", example = "Mypass1!")
    private String password;
}
