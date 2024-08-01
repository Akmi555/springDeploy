package org.blb.DTO.blog.blogs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Blog Comment data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentRequestDTO {
    @Schema(description = "id of the blog", example = "7")
    Long blogId;
    @Schema(description = "comment to blog", example = "some comment")
    String comment;
}
