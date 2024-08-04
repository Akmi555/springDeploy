package org.blb.controller.api.newsSection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.news.NewsSectionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/sections")
//@CrossOrigin(origins = "http://localhost:5173")
public interface FindNewsSectionApi {
    @Operation(summary = "Get all news sections", description = "The operation is available to everyone, return all news sections")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all news sections",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsSectionDTO.class))),

            @ApiResponse(responseCode = "404", description = "No news sections found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No news sections found\"}")))
    })

    @GetMapping
    ResponseEntity<List<NewsSectionDTO>> findAllNewsSections();
}
