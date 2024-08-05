package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "NewUser", description = "Data for registration")
public class UserNewDTO {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 25)
    @Schema(description = "Name of the user", example = "Jack")
    private String name;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!,.|?><~/])(?=\\S+$).{8,}$")
    @Schema(description = "User`s password", example = "Mypass1!")
    private String password;
    @Email
    @NotNull
    @Schema(description = "User`s email", example = "email@some.com")
    private String email;
    }
