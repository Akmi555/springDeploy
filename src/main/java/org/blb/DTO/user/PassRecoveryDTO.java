package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Request data for password recovery")
public class PassRecoveryDTO {
    @Schema(description = "some data from user email")
    String data;
    @Schema(description = "some code from user email")
    String code;
}
