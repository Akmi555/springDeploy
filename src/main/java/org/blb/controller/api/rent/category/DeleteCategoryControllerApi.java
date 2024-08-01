package org.blb.controller.api.rent.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface DeleteCategoryControllerApi {

    @Operation(summary = "Delete a category by ID", description = "Deletes a category from the catalog by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the product",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product deleted successfully\"}"))),
            @ApiResponse(responseCode = "403",
                    description = "User does not have permission to delete a category",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"You do not have permission to delete this category.\"}"))),
            @ApiResponse(responseCode = "404",
                    description = "Category not found with the given ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Category not found with id: ...\"}"))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Internal server error\"}")))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(
            @Parameter(description = "ID of the category to be deleted", example = "1")
            @PathVariable Long id);
}
