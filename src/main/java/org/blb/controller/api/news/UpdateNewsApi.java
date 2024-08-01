package org.blb.controller.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.news.NewsDataRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface UpdateNewsApi {

    @Operation(
            summary = "Updating news reaction (like/dislike)",
            description = "The operation is available to registered users. Add or remove a like/dislike for the news."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reaction for news with ID = ... updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class),
                            examples = @ExampleObject(value = "{ \"message\": \"Reaction for news with ID = 324 updated successfully\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Fields are empty OR have validation errors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(value = "{ \"errors\": [ { \"field\": \"liked\", \"rejectedValue\": null, \"message\": \"darf nicht null sein\" }, { \"field\": \"disliked\", \"rejectedValue\": null, \"message\": \"darf nicht null sein\" }, { \"field\": \"newsId\", \"rejectedValue\": null, \"message\": \"darf nicht null sein\" } ] }")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User must be registered to set a reaction for the news",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"timestamp\": \"2024-07-27T11:02:26.130+00:00\", \"status\": 403, \"error\": \"Forbidden\", \"path\": \"/api/news/reaction\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News with ID = ... not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"News with ID = 25 not found\" }")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict: News already has a reaction from this user or conflicting reactions",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Example 1", value = "{ \"message\": \"News already disliked by this user\" }"),
                                    @ExampleObject(name = "Example 2", value = "{ \"message\": \"Cannot like and dislike the news simultaneously\" }")
                            }
                    )
            )
    })
    @PutMapping("/reaction")
    ResponseEntity<StandardResponseDto> updateReaction(@Valid @RequestBody NewsDataRequestDto dto);
}