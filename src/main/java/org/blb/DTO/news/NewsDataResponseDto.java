package org.blb.DTO.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Response: News data")
public class NewsDataResponseDto {
    @Schema(description = "ID of the news", example = "461")
    private Long id;
    @Schema(description = "Region ID of the news", example = "8")
    private Long regionId;
    @Schema(description = "Region name of the news", example = "Hessen")
    private String regionName;
    @Schema(description = "Section name of the news", example = "inland")
    private String sectionName;
    @Schema(description = "Title of the news", example = "Stromausfall in Bad Homburg: Tausende Haushalte und Bahnhof betroffen")
    private String title;
    @Schema(description = "Date published of the news", example = "2024-07-27T12:46:53.250+02:00")
    private String date;
    @Schema(description = "Title image of the news (square: 1x1-840)", example = "https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlUSc/1x1-840.jpg")
    private String titleImageSquare;
    @Schema(description = "Title image of the news (wide: 16x9-960)", example = "https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlNY8/16x9-960.jpg")
    private String titleImageWide;
    @Schema(description = "HTML Content of the news", example = "<div className=\\\"textValueNews\\\"><strong>In Bad Homburg ist in der Nacht großflächig der Strom ausgefallen. Auch benachbarte Gemeinden war zum Teil betroffen.</strong></div> ...")
    private String content;
    @Schema(description = "Count of likes on the news", example = "10")
    private int likeCount;
    @Schema(description = "Count of dislikes on the news", example = "5")
    private int dislikeCount;
    @Schema(description = "Count of comments on the news", example = "1")
    private int commentsCount;
    @Schema(description = "True if news was liked by current user", example = "true")
    private Boolean like;
    @Schema(description = "True if news was disliked by current user", example = "false")
    private Boolean dislike;
}
