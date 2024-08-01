package org.blb.DTO.news;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
@Schema(name = "Request: update Reaction to news")
@Data
@AllArgsConstructor
public class NewsDataRequestDto {
    @Schema(description = "id of the news", example = "5")
    @NotNull
    @Min(1)
    Long newsId;

    @Schema(description = "Like status of the news", example = "true")
    @NotNull
    Boolean liked;

    @Schema(description = "Dislike status of the news", example = "false")
    @NotNull
    Boolean disliked;
}
