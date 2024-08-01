package org.blb.DTO.blog.blogs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Comment for blog")
public class BlogCommentResponseDTO
{
    @Schema(description = "id of the comment", example = "7")
    Long id;

    @Schema(description = "text of the comment", example = "You blog is good")
    private String comment;

    @Schema(description = "date of comment publishing", example = "20024-07-25 15:30")
    private LocalDateTime commentDate;

    @Schema(description = "Author`s name", example = "Tom")
    String authorName;

    @Schema(description = "Is comment published by current user", example = "true")
    Boolean isPublishedByCurrentUser;
}
