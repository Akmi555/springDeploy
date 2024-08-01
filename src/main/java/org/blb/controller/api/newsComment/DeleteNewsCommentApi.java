package org.blb.controller.api.newsComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface DeleteNewsCommentApi {
    @Operation(summary = "Deleting news comment by ID", description = "The operation is available to registered user, delete news comment by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about comment deleting",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"News Comment with ID = 2 deleted successfully\"}"))),

            @ApiResponse(responseCode = "404", description = "Comment with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Comment with id = 20 not found\"}"))),

            @ApiResponse(responseCode = "400", description = "Fields are empty OR have validation Errors",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"errors\": [\n" +
                                    "        {\n" +
                                    "            \"field\": \"id\",\n" +
                                    "            \"rejectedValue\": null,\n" +
                                    "            \"message\": \"darf nicht null sein\"\n" +
                                    "        }\n" +
                                    "    ]}"))),

            @ApiResponse(responseCode = "403", description = "User must be registered to delete this news comment",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\": \"2024-07-27T11:39:07.223+00:00\",\n" +
                                            "    \"status\": 403,\n" +
                                            "    \"error\": \"Forbidden\",\n" +
                                            "    \"path\": \"/api/news/comment\"}"))),

            @ApiResponse(responseCode = "409", description = "Conflict: User does not have permission to delete this comment because it was created by another user",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value =
                                            "\"message\": \"You don't have permission to delete this comment\"}")}
                    ))
    })
    @DeleteMapping("/comment")
    ResponseEntity<StandardResponseDto> deleteNewsCommentById(@Valid @RequestBody StandardDelRequest dto);
}
