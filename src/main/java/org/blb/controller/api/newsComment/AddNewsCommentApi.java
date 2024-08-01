package org.blb.controller.api.newsComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.newsComment.NewsCommentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/news")
//@CrossOrigin(origins = "http://localhost:5173")
public interface AddNewsCommentApi {
    @Operation(summary = "Adding new news comment", description = "The operation is available to registered user, add new news comment")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about news comment adding",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Comment to news with ID = 25 added successfully\"}"))),

            @ApiResponse(responseCode = "404", description = "News with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"News with ID = 25 not found\"}"))),

            @ApiResponse(responseCode = "400", description = "Fields are empty OR have validation Errors",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"errors\": [\n" +
                                    "        {\n" +
                                    "            \"field\": \"comment\",\n" +
                                    "            \"rejectedValue\": \"\",\n" +
                                    "            \"message\": \"darf nicht leer sein\"\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"field\": \"comment\",\n" +
                                    "            \"rejectedValue\": \"\",\n" +
                                    "            \"message\": \"Größe muss zwischen 2 und 2147483647 sein\"\n" +
                                    "        }\n" +
                                    "    ]}"))),

            @ApiResponse(responseCode = "403", description = "User must be registered to add a new news comment",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\": \"2024-07-27T11:39:07.223+00:00\",\n" +
                                    "    \"status\": 403,\n" +
                                    "    \"error\": \"Forbidden\",\n" +
                                    "    \"path\": \"/api/news/comment\"}")))

    })
    @PostMapping("/comment")
    ResponseEntity<StandardResponseDto> addNewsComment(@Valid @RequestBody NewsCommentRequestDTO DTO);
}
