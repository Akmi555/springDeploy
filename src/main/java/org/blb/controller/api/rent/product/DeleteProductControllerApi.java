package org.blb.controller.api.rent.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rent")
public interface DeleteProductControllerApi {

    @Operation(summary = "Delete a product by ID", description = "Deletes a product from the catalog by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product successfully deleted\"}"))),
            @ApiResponse(responseCode = "404", description = "Product not found with the given ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product with ID ... not found.\"}"))),
            @ApiResponse(responseCode = "409", description = "Only the author can delete their ad",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"You do not have permission to delete this product\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"timestamp\": \"2024-07-30T16:41:37.865+00:00\",\n" +
                                    "    \"status\": 403,\n" +
                                    "    \"error\": \"Forbidden\",\n" +
                                    "    \"path\": \"/api/rent/...\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Internal server error.\"}")))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<OneMessageDTO> deleteProductById(
            @Parameter(description = "ID of the product to be deleted", example = "1")
            @PathVariable Long id);
}
