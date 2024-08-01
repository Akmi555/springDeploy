package org.blb.controller.api.rent.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.categoryDto.CategoryResponseDto;
import org.springframework.http.ResponseEntity;


public interface FindCategoryControllerApi {

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories. If the list is empty, returns a message indicating that.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponseDto.class),
                            examples = @ExampleObject(value = "[{\"name\": \"Vehicles\"}, {\"name\": \"Electronics\"}]"))),
            @ApiResponse(responseCode = "204", description = "List of Categories is empty",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneMessageDTO.class),
                            examples = @ExampleObject(value = "{ \"message\": \"List of Categories is empty\" }"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneMessageDTO.class),
                            examples = @ExampleObject(value = "{ \"message\": \"Internal server error\" }")))
    })
    ResponseEntity<?> findAll();
}
