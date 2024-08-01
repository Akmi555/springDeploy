package org.blb.controller.api.rent.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public interface AddCategoryControllerApi {

    @Operation(summary = "Add a new category",
            description = "Allows an admin to add a new category to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OneMessageDTO.class),
                            examples = @ExampleObject(value = "{ \"message\": \"New category with name ... successfully created\"}"))),
            @ApiResponse(responseCode = "403", description = "User does not have permission to add a category",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"You do not have the rights to add a category.\"}"))),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Category with name ... already exists.\"}"))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Internal server error\"}")))
    })
    @PostMapping
    ResponseEntity<?> addCategory(@Valid @RequestBody CategoryCreateRequestDto requestDto);
}
