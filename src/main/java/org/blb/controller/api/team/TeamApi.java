package org.blb.controller.api.team;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.team.TeamResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/team")
public interface TeamApi {
    @Operation(summary = "Return members of our team", description = "The operation is available to everyone")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="User token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponse.class))),})
    @GetMapping()
    ResponseEntity<TeamResponse> getTeam();
}
