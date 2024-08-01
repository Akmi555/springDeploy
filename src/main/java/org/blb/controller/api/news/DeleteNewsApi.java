package org.blb.controller.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
//@CrossOrigin(origins = "http://localhost:5173")
public interface DeleteNewsApi {
    @Operation(summary = "Deleting news by ID", description = "The operation is available only for ADMIN, delete news by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about news deleting",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"News with ID = 2 deleted successfully\"}"))),

            @ApiResponse(responseCode = "404", description = "News with ID = .. not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"News with ID = 20 not found\"}"))),

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

            @ApiResponse(responseCode = "403", description = "User must be registered and has role 'ADMIN' to load news",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\": \"2024-07-27T11:39:07.223+00:00\",\n" +
                                            "    \"status\": 403,\n" +
                                            "    \"error\": \"Forbidden\",\n" +
                                            "    \"path\": \"/api/admin/news\"}")))
    })

    @DeleteMapping("/news")
    StandardResponseDto deleteNewsById(@Valid @RequestBody StandardDelRequest dto);
}
