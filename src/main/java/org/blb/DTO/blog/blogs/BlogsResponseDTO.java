package org.blb.DTO.blog.blogs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Schema(name = "Blogs", description = "Ten latest blogs")
public class BlogsResponseDTO {
    @Schema(description = "Count of page with blogs", example = "10")
    private Integer pageCount;
    @Schema(description = "Current page, start with 0", example = "0")
    private Integer currentPage;
    private List<BlogResponseDTO> blogs;
}
