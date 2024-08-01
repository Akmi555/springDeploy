package org.blb.DTO.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithIdDTO {
    @Min(0)
    @NotNull
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 15)
    private String name;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    @NotNull
    private String password;
    @Email
    @NotNull
    private String email;

}
