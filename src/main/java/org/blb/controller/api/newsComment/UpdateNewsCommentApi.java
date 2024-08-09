package org.blb.controller.api.newsComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.newsComment.UpdateCommentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface UpdateNewsCommentApi {
    @Operation(summary = "Updating news comment", description = "The operation is available to registered user, update comment on news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment with ID = ... updated successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Comment with ID = 32 updated successfully\"}"))),

            @ApiResponse(responseCode = "400", description = "Fields are empty OR have validation Errors",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{ \"errors\": [\n" +
                                    "        {\n" +
                                    "            \"field\": \"id\",\n" +
                                    "            \"rejectedValue\": null,\n" +
                                    "            \"message\": \"must not be null\"\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"field\": \"comment\",\n" +
                                    "            \"rejectedValue\": null,\n" +
                                    "            \"message\": \"must not be blank\"\n" +
                                    "        }\n" +
                                    "    ]}"))),

            @ApiResponse(responseCode = "403", description = "User must be registered to update this comment ",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\": \"2024-07-27T11:02:26.130+00:00\",\n" +
                                            "    \"status\": 403,\n" +
                                            "    \"error\": \"Forbidden\",\n" +
                                            "    \"path\": \"/api/news/reaction\"}"))),

            @ApiResponse(responseCode = "404", description = "Comment with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Comment with ID = 25 not found\"}"))),

            @ApiResponse(responseCode = "409", description = "Only author or admin can update comment",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"You don't have permission to update this comment\"}")))

    })
    @PutMapping("comment")
    ResponseEntity<StandardResponseDto> updateNewsCommentById(@Valid @RequestBody UpdateCommentRequestDTO DTO);
}
