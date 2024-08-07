package org.blb.DTO.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Team member")
public class TeamResponseDTO {
    @Schema (description = "Name of the team member", example = "Tom")
    private String name;
    @Schema (description = "Role of the team member", example = "Frontend")
    private String role;
    @Schema (description = "Describe what exactly team member done in project", example = "About")
    private String description;
    @Schema (description = "Photo of the team member", example = "www.site.com/photo.jpg")
    private String photoUrl;
    @Schema (description = "Url to the team member page", example = "some url")
    private String url;
}
