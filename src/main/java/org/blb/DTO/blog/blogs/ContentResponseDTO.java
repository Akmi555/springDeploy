package org.blb.DTO.blog.blogs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ContentResponseDTO {
    @Schema(description = "Blog title", example = "Some title")
    private String title;
    @Schema(description = "Date of publishing", example = "2024-07-23")
    private LocalDate publishedDate;
    @Schema(description = "Count of blog views", example = "1000")
    private Integer views;
    @Schema(description = "Author`s name of the blog", example = "Tom")
    private String authorName;
    @Schema(description = "Name of the region", example = "Brandenburg")
    private String regionName;
    @Schema(description = "content of a blog", example = "some content")
    private String content;

    List<BlogCommentResponseDTO> comments;
}
