package org.blb.DTO.newsComment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Request: update Comment on news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequestDTO {
    @Schema(description = "id of object", example = "7")
    @Min(1)
    @NotNull
    private Long id;

    @Schema(description = "comment to news", example = "some comment")
    @NotBlank
    @Size(min = 2)
    private String comment;
}
