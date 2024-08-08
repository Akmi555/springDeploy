package org.blb.DTO.blog.blogs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request with id of an element")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdRequestDTO {
    @Schema(description = "id of an element", example = "7")
    @NotNull
    Long id;
}
