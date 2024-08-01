package org.blb.DTO.mainPageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="Main Page information")
public class MpNewsResponse {
    @Schema(description = "id of news", example = "7")
    private Long id;
    @Schema(description = "Name of news region", example = "Berlin")
    private String regionName;
    @Schema(description = "News section", example = "Sport")
    private String sectionName;
    @Schema(description = "News title", example = "Some news title")
    private String title;
    @Schema(description = "Date of the news", example = "2024.07.24")
    private String date;
    @Schema(description = "News image", example = "https://site.com/image.jpg")
    private String titleImageSquare;
    @Schema(description = "Count of likes", example = "100")
    private int likeCount;
    @Schema(description = "Count of dislikes", example = "10")
    private int dislikeCount;
    @Schema(description = "Count of comments", example = "10")
    private int commentsCount;
}
