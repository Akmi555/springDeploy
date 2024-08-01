package org.blb.controller.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
//@CrossOrigin(origins = "http://localhost:5173")
public interface AddNewsApi {
    @Operation(summary = "Loading new news to database", description = "The operation is available only for ADMIN, load new news to database")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about news loading",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"All news loaded successfully\"}"))),

            @ApiResponse(responseCode = "204", description = "No Content: The request was successfully processed, but there is no data to return",
                    content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"Fetch response contains no data\"}"))),

            @ApiResponse(responseCode = "403", description = "User must be registered and has role 'ADMIN' to load news",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\": \"2024-07-27T11:39:07.223+00:00\",\n" +
                                            "    \"status\": 403,\n" +
                                            "    \"error\": \"Forbidden\",\n" +
                                            "    \"path\": \"/api/admin/news\"}"))),

            @ApiResponse(responseCode = "500", description = "Internal Server Error: Various server errors can occur during processing.",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "JSON Processing Error", value = "{\"message\": \"Error processing JSON response: error details here\"}"),
                                    @ExampleObject(name = "Details URL JSON Processing Error", value = "{\"message\": \"Error processing details URL JSON: error details here\"}"),
                                    @ExampleObject(name = "Fetching Data from Details URL Error", value = "{\"message\": \"Error fetching data from details URL: http://example.com/details\"}"),
                                    @ExampleObject(name = "Fetching Data from URL Error", value = "{\"message\": \"Error fetching data from URL: http://example.com\"}")
                            }
                    )
            )
    })
    @PostMapping("/news")
    StandardResponseDto loadAllNewsFromAPIsToDataBase();
}
