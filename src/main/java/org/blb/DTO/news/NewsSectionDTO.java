package org.blb.DTO.news;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Response: News Section")
public class NewsSectionDTO {
    @Schema(description = "News section ID", example = "1")
    @NotNull(message = "News section ID must be not null.")
    private Long id;

    @Schema(description = "News Section name", example = "inland")
    @NotNull(message = "News section name must be not null.")
    @NotBlank(message = "News section name must be not blank.")
    private String sectionName;
}
