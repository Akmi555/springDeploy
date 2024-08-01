package org.blb.DTO.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.blb.models.blog.Blog;
import org.blb.models.region.Region;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "blog for update")
public class BlogUpdateDTO {
    @Schema(description = "id of the blog", example = "7")
    Long id;
    @Schema(description = "data of blog")
    BlogAddRequestDTO blogAddRequestDTO;
    public void DtoToBlog(Blog blog, Region region){
        blog.setContent(blogAddRequestDTO.getContent());
        blog.setTitle(blogAddRequestDTO.getTitle());
        blog.setRegion(region);
        blog.setEditedDate(LocalDateTime.now());
    }
}
