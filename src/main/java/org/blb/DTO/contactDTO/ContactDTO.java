package org.blb.DTO.contactDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Request: Create contact form", description = "Data for contact form")
public class ContactDTO {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 15, message = "Name must contain between 3 and 15 characters")
    @Schema(description = "Name of the contact person", example = "Hanna")
    private String contactName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    @Schema(description = "Contact person's email", example = "email@some.com")
    private String contactEmail;

    @NotBlank(message = "Message must not be blank")
    @Schema(description = "Message from the contact person", example = "Example text")
    private String contactMessage;
}
