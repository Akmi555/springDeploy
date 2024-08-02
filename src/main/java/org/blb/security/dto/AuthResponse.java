package org.blb.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Authorization answer")
public class AuthResponse {
    @Schema(description = "Authorization token", example = "some token")
    private String token;
    @Schema(description = "User role", example = "ROLE_ADMIN")
    private String role;
}
