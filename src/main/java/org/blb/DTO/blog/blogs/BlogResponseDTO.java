package org.blb.DTO.blog.blogs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.blb.models.region.Region;
import org.blb.models.user.User;

import java.time.LocalDate;

@Data
@Schema(name = "Blog data")
public class BlogResponseDTO {
    @Schema(description = "Blog id", example = "7")
    private Long id;
    @Schema(description = "Blog title", example = "Some title")
    private String title;
    @Schema(description = "Date of publishing", example = "2024-07-23")
    private LocalDate publishedDate;
    @Schema(description = "Count of blog views", example = "1000")
    private Integer views;
    @Schema(description = "Count of blog comment", example = "10")
    private Integer comments;
    @Schema(description = "Author`s name of the blog", example = "Tom")
    private String authorName;
    @Schema(description = "Name of the region", example = "Brandenburg")
    private String regionName;

    public BlogResponseDTO(Long id, String title, LocalDate publishedDate,
                           Integer views, Integer comments, User author, Region region) {
        this.id = id;
        this.title = title;
        this.publishedDate = publishedDate;
        this.views = views;
        this.comments = comments;
        this.authorName = author.getName();
        this.regionName = region.getRegionName();
    }


}

