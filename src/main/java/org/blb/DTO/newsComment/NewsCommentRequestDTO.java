package org.blb.DTO.newsComment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Request: create Comment on news")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCommentRequestDTO {
    @Schema(description = "comment to news", example = "some comment")
    @NotNull
    @NotBlank
    @Size(min=2)
    private String comment;

    @Schema(description = "id of the news", example = "5")
    @NotNull
    @Min(1)
    private Long newsId;
}
