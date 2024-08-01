package org.blb.DTO.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Schema(name = "Response: News data page", description = " 'N' latest news by page")
public class NewsDataPageResponseDto {
    @Schema(description = "Count of page with news", example = "10")
    private Integer pageCount;
    @Schema(description = "Current page, start with 0", example = "0")
    private Integer currentPage;
    private List<NewsDataResponseDto> newsDataPage;
}
